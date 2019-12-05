package pl.wut.wsd.dsm.agent.quote_manager;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

@Slf4j
public class QuoteAgent extends Agent {

    private final CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    private final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);

    @Override
    protected void setup() {
        this.addBehaviour(new MessageHandler(this,
                MessageSpecification.of(customerDraftProtocol.sendClientDecision().toMessageTemplate(), this::processClientResponse))
        );
        registerToWhitepages();
    }

    private void processClientResponse(final ACLMessage message) {
        log.info("Processing client response");
    }

    private void registerToWhitepages() {
        final DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(customerDraftProtocol.sendClientDecision().getTargetService());
        try {
            DFService.register(this, dfAgentDescription);
        } catch (final FIPAException e) {
            log.error("Could not register to whitepages", e);
        }
    }
}
