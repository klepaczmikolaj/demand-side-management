package pl.wut.wsd.dsm.agent.customer_handler;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_handler.handler.CustomerOfferHandler;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

@Slf4j
public class CustomerHandlerAgent extends Agent {

    @Getter
    private Customer customer;
    private CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    @Getter
    private final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);

    private CustomerOfferHandler customerOfferHandler;

    @Override
    protected void setup() {
        final CustomerHandlerDependencies dependencies = (CustomerHandlerDependencies) getArguments()[0];
        this.customer = dependencies.getCustomer();

        this.customerOfferHandler = new CustomerOfferHandler(dependencies.getCodec(), new CustomerDraftProtocol().sendCustomerOffer(), this, serviceDiscovery, customerOfferRepository);
        this.addBehaviour(
                new MessageHandler(this,
                        MessageSpecification.of(customerDraftProtocol.sendOfferToHandler().toMessageTemplate(), customerOfferHandler::handle),
                        MessageSpecification.of(customerDraftProtocol.acceptClientDecision().toMessageTemplate(), this::handleAcceptance))
        );
        registerToWhitepages();
    }

    private void handleAcceptance(final ACLMessage aclMessage) {
        log.info("User offer accepted: {}", aclMessage.getContent());
        customerDraftProtocol.acceptClientDecision().serviceDescription(customer);
    }

    private void registerToWhitepages() {
        final DFAgentDescription description = new DFAgentDescription();
        description.setName(this.getAID());
        description.addServices(customerDraftProtocol.sendOfferToHandler().serviceDescription(customer));
        description.addServices(customerDraftProtocol.acceptClientDecision().serviceDescription(customer));

        try {
            DFService.register(this, description);
        } catch (final FIPAException e) {
            log.error("Could not connect to whitepages", e);
        }
    }


}
