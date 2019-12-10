package pl.wut.wsd.dsm.agent.quote_manager;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.infrastructure.messaging.OneShotMessageSpec;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.protocol.SystemDraftProtocol;
import pl.wut.wsd.dsm.protocol.customer_trust.GetCustomerTrustProtocol;

import java.util.List;
import java.util.UUID;

@Slf4j
public class QuoteAgent extends Agent {

    private QuoteAgentDependencies dependencies;
    private final CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    private final SystemDraftProtocol systemDraftProtocol = new SystemDraftProtocol();
    private final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);
    private final MessageHandler messageHandler = (new MessageHandler(this,
            MessageSpecification.of(customerDraftProtocol.sendClientDecision().toMessageTemplate(), this::processClientResponse),
            MessageSpecification.of(systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().toMessageTemplate(), this::processNewDraft)
    ));

    @Override
    protected void setup() {
        this.dependencies = (QuoteAgentDependencies) getArguments()[0];
        this.addBehaviour(messageHandler);
        registerToWhitepages();
    }

    private void processNewDraft(final ACLMessage aclMessage) {
        final Class<ExpectedInbalancement> messageClass = systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().getMessageClass();
        final Codec codec = dependencies.codec();
        final Result<ExpectedInbalancement, DecodingError> decodingResult = codec.decode(aclMessage.getContent(), messageClass);

        if (!decodingResult.isValid()) {
            log.error("Could not decode incoming message {}", decodingResult.error());
        } else {
            final ExpectedInbalancement inbalancement = decodingResult.result();
            final Result<List<DFAgentDescription>, FIPAException> trustServiceSearchResult = serviceDiscovery.findServices(GetCustomerTrustProtocol.customerTrustRequest.getTargetService());

            if (trustServiceSearchResult.isError()) {
                log.error("DF search error", trustServiceSearchResult.error());
            } else if (trustServiceSearchResult.result().isEmpty()) {
                log.info("No trust service found");
            } else {
                final DFAgentDescription trustServiceAgent = trustServiceSearchResult.result().get(0);

                final ACLMessage trustRequest = GetCustomerTrustProtocol.customerTrustRequest.templatedMessage();
                final GetTrustRankingRequest getTrustRankingRequest = new GetTrustRankingRequest();
                getTrustRankingRequest.setFrom(0);
                getTrustRankingRequest.setTo(Integer.MAX_VALUE);
                getTrustRankingRequest.setSelectionType(GetTrustRankingRequest.SelectionType.REGULAR);

                trustRequest.setContent(codec.encode(getTrustRankingRequest));
                trustRequest.addReceiver(trustServiceAgent.getName());
                trustRequest.setConversationId(UUID.randomUUID().toString());

                log.info("Sending message to trust agent", trustRequest);
                send(trustRequest);

                //Remove message handler behaviour to process trust agent response
                final MessageTemplate responseTemplate = MessageTemplate.and(
                        GetCustomerTrustProtocol.customerTrustRankingResponse.toMessageTemplate(),
                        MessageTemplate.MatchConversationId(trustRequest.getConversationId())
                );

                messageHandler.addSpecification(new OneShotMessageSpec(responseTemplate, this::continueProcessAfterTrustResponse));
            }
        }
    }

    private void continueProcessAfterTrustResponse(final ACLMessage aclMessage) {
        log.info("GOT TRUST RESPONSE!");
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
