package pl.wut.wsd.dsm.agent.customer_agent;

import io.javalin.Javalin;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_agent.core.CurrentUsageSender;
import pl.wut.wsd.dsm.agent.customer_agent.core.CurrentUsageSenderImpl;
import pl.wut.wsd.dsm.agent.customer_agent.core.CustomerObligationService;
import pl.wut.wsd.dsm.agent.customer_agent.core.OffersService;
import pl.wut.wsd.dsm.agent.customer_agent.device.Devices;
import pl.wut.wsd.dsm.agent.customer_agent.notification.NotificationAdapter;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.repo.HibernateCustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.repo.HibernateOfferRepository;
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
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumptionProtocol;
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

    @Override
    protected void setup() {
        final CustomerAgentDependencies dependencies = (CustomerAgentDependencies) getArguments()[0];
        javalin = dependencies.getJavalin();
        customer = dependencies.getCustomer();
        codec = dependencies.getCodec();
        final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);
        final AgentMessagingCapability capability = AgentMessagingCapability.defaultCapability(serviceDiscovery, this);
        final CustomerObligationService offerObligationService = new CustomerObligationService(
                capability,
                codec,
                customer,
                new ApiTypesMapper(),
                customerDraftProtocol.acceptClientDecision(),
                new HibernateCustomerObligationRepository(dependencies.getHibernateTemplate()),
                new HibernateOfferRepository(dependencies.getHibernateTemplate())
        );
        notificationAdapter = dependencies.getNotificationAdapter();

        this.offersService = offerObligationService;
        final DefaultCustomerApiHandle handle = new DefaultCustomerApiHandle(offerObligationService, offerObligationService, new SettingsService(), devices, customer, notificationAdapter);
        new ApiInitializer().initialize(dependencies.getJavalin(), handle);

        final CurrentUsageSender currentUsageSender = new CurrentUsageSenderImpl(devices,
                EnergyConsumptionProtocol.INSTANCE.informationStep(customer.getCustomerId()),
                serviceDiscovery, codec, capability);

        addBehaviour(new MessageHandler(this,
                MessageSpecification.of(customerDraftProtocol.sendCustomerOffer().toMessageTemplate(), this::processClientOffer),
                MessageSpecification.of(customerDraftProtocol.informOfCustomerHandlerAcceptance().toMessageTemplate(), this::saveObligation)
        ));

        addBehaviour(new TickerBehaviour(this, Duration.ofSeconds(30).toMillis()) {
            @Override
            protected void onTick() {
                currentUsageSender.sendToCustomerHandler();
            }
        });
        registerToWhitepages(customer.getCustomerId(), dependencies.getJavalinPort());
    }

    private void saveObligation(final ACLMessage aclMessage) {
        log.info("Got obligation acceptance from handler!");
//        final CustomerObligation obligation = codec.decode(aclMessage.getContent(), customerDraftProtocol.informOfCustomerHandlerAcceptance().getMessageClass()).result();
        //TODO maybe notification
    }


    private void processClientOffer(final ACLMessage aclMessage) {
        log.info("Got client offer!");
        final CustomerOffer offer = codec.decode(aclMessage.getContent(), customerDraftProtocol.sendCustomerOffer().getMessageClass()).result();

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
        final Property cusId = new Property("customerId", customerId);
        final Property restApiAddress = new Property(customerApiAddressProperty, String.format("http://%s:%d/", InetUtils.getMyHostname().orElse("localhost"), javalinPort));

        new ServiceRegistration(this).registerRetryOnFailure(Duration.ofSeconds(3),
                serviceDescriptionFactory.nameAndProperties("customer-agent", cusId, restApiAddress),
                customerDraftProtocol.sendCustomerOffer().serviceDescription(customer),
                customerDraftProtocol.informOfCustomerHandlerAcceptance().serviceDescription(customer));
    }
}
