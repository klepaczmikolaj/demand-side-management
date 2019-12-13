package pl.wut.wsd.dsm.agent.customer_agent;

import io.javalin.Javalin;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_agent.core.CustomerObligationService;
import pl.wut.wsd.dsm.agent.customer_agent.core.ObligationsService;
import pl.wut.wsd.dsm.agent.customer_agent.core.OffersService;
import pl.wut.wsd.dsm.agent.customer_agent.device.Device;
import pl.wut.wsd.dsm.agent.customer_agent.device.Devices;
import pl.wut.wsd.dsm.agent.customer_agent.rest.ApiInitializer;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.ApiError;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerSettings;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.settings.SettingsService;
import pl.wut.wsd.dsm.agent.infrastructure.InetUtils;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CustomerAgent extends Agent {

    private final ServiceDescriptionFactory serviceDescriptionFactory = new ServiceDescriptionFactory();
    private final CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    private final static String customerApiAddressProperty = "customerApiAddress";
    private final SettingsService settingsService = new SettingsService();
    private OffersService offersService;
    private ObligationsService obligationsService;
    private final Devices devices = new Devices();
    private Javalin javalin;
    private Customer customer;
    private Codec codec;

    @Override
    protected void setup() {
        final CustomerAgentDependencies dependencies = (CustomerAgentDependencies) getArguments()[0];
        final CustomerObligationService offerObligationService = new CustomerObligationService();
        offersService = offerObligationService;
        obligationsService = offerObligationService;
        javalin = dependencies.getJavalin();
        javalin.get("/", ctx -> ctx.result("Hello, agent " + getAID().getName() + " speaking"));
        customer = dependencies.getCustomer();
        codec = dependencies.getCodec();

        new ApiInitializer().initialize(dependencies.getJavalin(), new CustomerAgentApiHandle() {
            @Override
            public Result<ObligationRepresentation, ApiError> postObligation(final ObligationAcceptanceRequest acceptance) {
                return obligationsService.acceptObligation(acceptance);
            }

            @Override
            public Result<List<Device>, ApiError> getDevices() {
                return Result.ok(devices.getDevices());
            }

            @Override
            public Result<List<ObligationRepresentation>, ApiError> getObligationHistory() {
                return Result.ok(obligationsService.getObligationHistory());
            }

            @Override
            public Result<CustomerOfferRepresentation, ApiError> getCurrentOffer() {
                return offersService.getCurrentOffer().<Result<CustomerOfferRepresentation, ApiError>>map(Result::ok).
                        orElseGet(() -> Result.error(ApiError.notFound("No current offer for customer")));
            }

            @Override
            public Result<List<CustomerOfferRepresentation>, ApiError> getOffersHistory() {
                return Result.ok(offersService.getOfferHistory());
            }

            @Override
            public Result<ObligationRepresentation, ApiError> getCurrentObligation() {
                final Optional<ObligationRepresentation> obligation = obligationsService.getCurrentObligation();

                return obligation.<Result<ObligationRepresentation, ApiError>>map(Result::ok)
                        .orElseGet(() -> Result.error(ApiError.notFound("No obligation is subject to user realization")));
            }

            @Override
            public Result<CustomerSettings, ApiError> updateCustomerSettings(final CustomerSettings customerSettings) {
                if (customerSettings.getMinimalProfit() <= 0) {
                    return Result.error(ApiError.badRequest("Minimal profit must be greater than 0"));
                }
                return Result.ok(settingsService.setMinimalProfil(customerSettings.getMinimalProfit()));
            }

            @Override
            public Result<CustomerSettings, ApiError> getCustomerSettings() {
                return Result.ok(settingsService.getSettings());
            }

            @Override
            public Result<Device, ApiError> switchDevice(final Long id, final boolean on) {
                final Optional<Device> deviceOpt = devices.findById(id);
                if (!deviceOpt.isPresent()) {
                    return Result.error(ApiError.notFound(String.format("Device of id %d not found", id)));
                } else {
                    deviceOpt.get().setOn(on);
                    return Result.ok(deviceOpt.get());
                }
            }
        });

        addBehaviour(new MessageHandler(this, MessageSpecification.of(customerDraftProtocol.sendCustomerOffer().toMessageTemplate(), this::processClientOffer)));
        registerToWhitepages(customer.getCustomerId(), dependencies.getJavalinPort());
    }

    private void processClientOffer(final ACLMessage aclMessage) {
        log.info("Got client offer!");
        final CustomerOffer offer = codec.decode(aclMessage.getContent(), customerDraftProtocol.sendCustomerOffer().getMessageClass()).result();
        offersService.registerOffer(offer);
        //Send customer notification
    }

    @Override
    public void clean(final boolean ok) {
        super.clean(ok);
        try {
            DFService.deregister(this);
            javalin.stop();
            System.exit(0);
        } catch (final FIPAException e) {
            javalin.stop();
            System.exit(0);
            log.error("Could not deregister from whitepages");
        }
    }

    private void registerToWhitepages(final Long customerId, final int javalinPort) {
        final DFAgentDescription dfAgentDescription = new DFAgentDescription();
        final Property cusId = new Property("customerId", customerId);
        final Property restApiAddress = new Property(customerApiAddressProperty, String.format("http://%s:%d/", InetUtils.getMyHostname().orElse("localhost"), javalinPort));

        dfAgentDescription.addServices(serviceDescriptionFactory.nameAndProperties("customer-agent", cusId, restApiAddress));
        dfAgentDescription.addServices(customerDraftProtocol.sendCustomerOffer().serviceDescription(customer));
        try {
            DFService.register(this, dfAgentDescription);
        } catch (final FIPAException e) {
            log.error("Could not register to whitepages", e);
        }
    }
}
