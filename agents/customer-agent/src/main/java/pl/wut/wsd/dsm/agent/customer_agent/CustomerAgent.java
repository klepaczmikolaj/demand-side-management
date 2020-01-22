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
import pl.wut.wsd.dsm.agent.customer_agent.core.OffersService;
import pl.wut.wsd.dsm.agent.customer_agent.core.history.ObigationHistoryLocalStore;
import pl.wut.wsd.dsm.agent.customer_agent.core.history.OfferHistoryLocalStore;
import pl.wut.wsd.dsm.agent.customer_agent.device.Devices;
import pl.wut.wsd.dsm.agent.customer_agent.notification.NotificationAdapter;
import pl.wut.wsd.dsm.agent.customer_agent.rest.ApiInitializer;
import pl.wut.wsd.dsm.agent.customer_agent.rest.mapper.ApiTypesMapper;
import pl.wut.wsd.dsm.agent.customer_agent.settings.SettingsService;
import pl.wut.wsd.dsm.agent.infrastructure.InetUtils;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceRegistration;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.infrastructure.messaging.handle.AgentMessagingCapability;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

import java.time.Duration;


@Slf4j
public class CustomerAgent extends Agent {

    private final ServiceDescriptionFactory serviceDescriptionFactory = new ServiceDescriptionFactory();
    private final CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    private final static String customerApiAddressProperty = "customerApiAddress";
    private final Devices devices = new Devices();
    private OffersService offersService;
    private Javalin javalin;
    private Customer customer;
    private Codec codec;
    private NotificationAdapter notificationAdapter;
    private ObigationHistoryLocalStore obligations;
    private OfferHistoryLocalStore offers;

    @Override
    protected void setup() {
        final CustomerAgentDependencies dependencies = (CustomerAgentDependencies) getArguments()[0];
        obligations = new ObigationHistoryLocalStore();
        offers = new OfferHistoryLocalStore();
        javalin = dependencies.getJavalin();
        customer = dependencies.getCustomer();
        codec = dependencies.getCodec();
        final AgentMessagingCapability capability = AgentMessagingCapability.defaultCapability(new ServiceDiscovery(this), this);
        final CustomerObligationService offerObligationService = new CustomerObligationService(
                capability,
                codec,
                customer,
                new ApiTypesMapper(),
                customerDraftProtocol.acceptClientDecision(),
                obligations,
                offers
        );
        notificationAdapter = dependencies.getNotificationAdapter();

        this.offersService = offerObligationService;
        final DefaultCustomerApiHandle handle = new DefaultCustomerApiHandle(offerObligationService, offerObligationService, new SettingsService(), devices);
        new ApiInitializer().initialize(dependencies.getJavalin(), handle);

        addBehaviour(new MessageHandler(this,
                MessageSpecification.of(customerDraftProtocol.sendCustomerOffer().toMessageTemplate(), this::processClientOffer),
                MessageSpecification.of(customerDraftProtocol.informOfCustomerHandlerAcceptance().toMessageTemplate(), this::saveObligation)
        ));
        registerToWhitepages(customer.getCustomerId(), dependencies.getJavalinPort());
    }

    private void saveObligation(final ACLMessage aclMessage) {
        log.info("Got obligation acceptance from handler!");
        final CustomerObligation obligation = codec.decode(aclMessage.getContent(), customerDraftProtocol.informOfCustomerHandlerAcceptance().getMessageClass()).result();
        obligations.registerCurrentObligation(obligation);
    }


    private void processClientOffer(final ACLMessage aclMessage) {
        log.info("Got client offer!");
        final CustomerOffer offer = codec.decode(aclMessage.getContent(), customerDraftProtocol.sendCustomerOffer().getMessageClass()).result();
        offersService.registerOffer(offer);
//        notificationAdapter.sendNotification(new CustomerNotification("Masz nową wiadomość mordo", codec.encode(offer)));
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

        new ServiceRegistration(this).registerRetryOnFailure(Duration.ofSeconds(3),
                serviceDescriptionFactory.nameAndProperties("customer-agent", cusId, restApiAddress),
                customerDraftProtocol.sendCustomerOffer().serviceDescription(customer),
                customerDraftProtocol.informOfCustomerHandlerAcceptance().serviceDescription(customer));
    }
}
