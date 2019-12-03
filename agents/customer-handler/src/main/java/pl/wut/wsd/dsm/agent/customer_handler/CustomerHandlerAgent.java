package pl.wut.wsd.dsm.agent.customer_handler;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.protocol.DraftProtocol;

@Slf4j
public class CustomerHandlerAgent extends Agent {

    private Customer customer;
    private DraftProtocol draftProtocol;

    @Override
    protected void setup() {
        final CustomerHandlerDependencies dependencies = (CustomerHandlerDependencies) getArguments()[0];
        this.customer = dependencies.getCustomer();
        this.draftProtocol = DraftProtocol.forCustomer(customer);
        this.addBehaviour(
                new MessageHandler(this, MessageSpecification.of(draftProtocol.sendClientOffer().toMessageTemplate(), this::handleCustomerOffer))
        );
        this.addBehaviour(
                new MessageHandler(this, MessageSpecification.of(draftProtocol.acceptClientDecision().toMessageTemplate(), this::handleAcceptance))
        );
        registerToWhitepages();
    }

    private void handleAcceptance(final ACLMessage aclMessage) {
        log.info("User offer accepted: {}", aclMessage.getContent());
        draftProtocol.acceptClientDecision().getTargetService();
    }

    private void registerToWhitepages() {
        final DFAgentDescription description = new DFAgentDescription();
        description.setName(this.getAID());
        description.addServices(draftProtocol.sendClientOffer().getTargetService());

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
