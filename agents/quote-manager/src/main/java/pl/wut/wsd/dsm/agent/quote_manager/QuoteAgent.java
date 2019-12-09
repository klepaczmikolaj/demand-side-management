package pl.wut.wsd.dsm.agent.quote_manager;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.protocol.SystemDraftProtocol;

@Slf4j
public class QuoteAgent extends Agent {

    private QuoteAgentDependencies dependencies;
    private final CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    private final SystemDraftProtocol systemDraftProtocol = new SystemDraftProtocol();
    private final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);

    @Override
    protected void setup() {
        this.dependencies = (QuoteAgentDependencies) getArguments()[0];
        this.addBehaviour(new MessageHandler(this,
                MessageSpecification.of(customerDraftProtocol.sendClientDecision().toMessageTemplate(), this::processClientResponse),
                MessageSpecification.of(systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().toMessageTemplate(), this::processNewDraft)
        ));
        registerToWhitepages();
    }

    private void processNewDraft(final ACLMessage aclMessage) {
        final Class<ExpectedInbalancement> messageClass = systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().getMessageClass();
        final Codec codec = dependencies.codec();
        final Result<ExpectedInbalancement, DecodingError> decodingResult = codec.decode(aclMessage.getContent(), messageClass);

        if (!decodingResult.isValid()) {
            log.error("Could not decode incoming message", decodingResult.error());
        } else {
            final ExpectedInbalancement result = decodingResult.result();

            // Find customers by ranking, for each of them prepare offer
//            customerDraftProtocol.sendClientOffer().serviceDescription()
        }
    }

    private void processClientResponse(final ACLMessage message) {
        log.info("Processing client response");
    }

    private void registerToWhitepages() {
        final DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(customerDraftProtocol.sendClientDecision().getTargetService());
        dfAgentDescription.addServices(systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().getTargetService());
        try {
            DFService.register(this, dfAgentDescription);
        } catch (final FIPAException e) {
            log.error("Could not register to whitepages", e);
        }
    }
}
