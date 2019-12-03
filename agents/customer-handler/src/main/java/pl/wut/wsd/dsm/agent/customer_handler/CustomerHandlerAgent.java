package pl.wut.wsd.dsm.agent.customer_handler;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

@Slf4j
public class CustomerHandlerAgent extends Agent {

    private Customer customer;
    private CustomerDraftProtocol customerDraftProtocol;
    private final ServiceDiscovery<CustomerHandlerAgent> serviceDiscovery = new ServiceDiscovery<>(this);

    @Override
    protected void setup() {
        final CustomerHandlerDependencies dependencies = (CustomerHandlerDependencies) getArguments()[0];
        this.customer = dependencies.getCustomer();
        this.customerDraftProtocol = CustomerDraftProtocol.forCustomer(customer);
        this.addBehaviour(
                new MessageHandler(this, MessageSpecification.of(customerDraftProtocol.sendClientOffer().toMessageTemplate(), this::handleCustomerOffer))
        );
        this.addBehaviour(
                new MessageHandler(this, MessageSpecification.of(customerDraftProtocol.acceptClientDecision().toMessageTemplate(), this::handleAcceptance))
        );
        registerToWhitepages();
    }

    private void handleAcceptance(final ACLMessage aclMessage) {
        log.info("User offer accepted: {}", aclMessage.getContent());
        customerDraftProtocol.acceptClientDecision().getTargetService();
    }

    private void registerToWhitepages() {
        final DFAgentDescription description = new DFAgentDescription();
        description.setName(this.getAID());
        description.addServices(customerDraftProtocol.sendClientOffer().getTargetService());

        try {
            DFService.register(this, description);
        } catch (final FIPAException e) {
            log.error("Could not connect to whitepages", e);
        }
    }

    private void handleCustomerOffer(final ACLMessage aclMessage) {
        log.info("Received customer offer: ", aclMessage.getContent());
        //TODO, send offer to customer and wait for response.
    }


}
