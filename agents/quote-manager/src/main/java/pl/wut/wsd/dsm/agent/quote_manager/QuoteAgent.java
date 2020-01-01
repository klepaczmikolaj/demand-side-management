package pl.wut.wsd.dsm.agent.quote_manager;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.quote_manager.draft.DraftManagement;
import pl.wut.wsd.dsm.agent.quote_manager.offer.OfferPreparer;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceRegistration;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.infrastructure.messaging.OneShotMessageSpec;
import pl.wut.wsd.dsm.infrastructure.messaging.handle.AgentMessagingCapability;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRankingEntry;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.protocol.SystemDraftProtocol;
import pl.wut.wsd.dsm.protocol.customer_trust.GetCustomerTrustProtocol;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
public class QuoteAgent extends Agent {

    private Codec codec;
    private final CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    private final SystemDraftProtocol systemDraftProtocol = new SystemDraftProtocol();
    private final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);
    private final MessageHandler messageHandler = (new MessageHandler(this,
            MessageSpecification.of(customerDraftProtocol.sendClientDecision().toMessageTemplate(), this::processClientResponse),
            MessageSpecification.of(systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().toMessageTemplate(), this::processInbalancement)
    ));
    private final DraftManagement draftManagement = new DraftManagement();
    private final AgentMessagingCapability messagingCapability = AgentMessagingCapability.defaultCapability(serviceDiscovery, this);
    private final OfferPreparer offerPreparer = new OfferPreparer();

    @Override
    protected void setup() {
        final QuoteAgentDependencies dependencies = (QuoteAgentDependencies) getArguments()[0];
        this.codec = dependencies.codec();
        this.addBehaviour(messageHandler);
        this.addBehaviour(new UpdatesNetworkAdvisorWithDraftInfo(codec, draftManagement, this, serviceDiscovery));

        registerToWhitepages();
    }

    private void processInbalancement(final ACLMessage aclMessage) {
        if (draftManagement.currentDraftStillInProgress()) {
            log.info("Current draft is still in progress, new draft wont be started, current: {}", draftManagement.getSummaryStatistics());
            return;
        }

        final Class<ExpectedInbalancement> messageClass = systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().getMessageClass();
        final Result<ExpectedInbalancement, DecodingError> decodingResult = codec.decode(aclMessage.getContent(), messageClass);

        if (decodingResult.isError()) {
            log.error("Could not decode incoming message {}", decodingResult.error());
        } else {
            final ExpectedInbalancement inbalancement = decodingResult.result();

            draftManagement.startNewDraft(inbalancement.getSince(), inbalancement.getUntil());

            final ACLMessage trustRequest = GetCustomerTrustProtocol.customerTrustRequest.templatedMessage();
            trustRequest.setContent(codec.encode(GetTrustRankingRequest.requestWholeRanking()));
            trustRequest.setConversationId(UUID.randomUUID().toString());

            messagingCapability.send(trustRequest, GetCustomerTrustProtocol.customerTrustRequest.getTargetService());

            log.info("Sending message to trust agent", trustRequest);

            registerResponseHandler(trustRequest.getConversationId(), GetCustomerTrustProtocol.customerTrustRankingResponse.toMessageTemplate(), message -> continueProcessAfterTrustResponse(message, inbalancement));
        }
    }

    private void continueProcessAfterTrustResponse(final ACLMessage aclMessage, final ExpectedInbalancement expectedInbalancement) {
        final Class<CustomerTrustRanking> messageClass = GetCustomerTrustProtocol.customerTrustRankingResponse.getMessageClass();

        final Result<CustomerTrustRanking, DecodingError> decodingResult = codec.decode(aclMessage.getContent(), messageClass);
        if (!decodingResult.isValid()) {
            log.error("Could not decode trust response", decodingResult.error());
        } else {
            final List<CustomerTrustRankingEntry> ranking = decodingResult.result().getRankingEntries();
            final Map<Long, CustomerOffer> offerByCustomerId = offerPreparer.prepareCustomerOffers(ranking, expectedInbalancement);

            draftManagement.registerClientOffers(new HashSet<>(offerByCustomerId.values()));

            offerByCustomerId.forEach(this::sendCustomerOffer);
        }
    }

    private void sendCustomerOffer(final Long customerId, final CustomerOffer customerOffer) {
        final ServiceDescription customerService = customerDraftProtocol.sendOfferToHandler().serviceDescription(new Customer(customerId));
        final ACLMessage message = customerDraftProtocol.sendOfferToHandler().templatedMessage();
        message.setConversationId(UUID.randomUUID().toString());
        message.setContent(codec.encode(customerOffer));
        final Result<Set<AID>, FIPAException> sendResult = messagingCapability.send(message, customerService);
        if (sendResult.isValid()) {
            log.info("Sent customer offer to customer {}", customerId);
            registerResponseHandler(message.getConversationId(), customerDraftProtocol.sendClientDecision().toMessageTemplate(), this::processClientResponse);
        } else {
            log.info("Could not send customer offer {}", sendResult.error().getMessage());
        }
    }

    private void processClientResponse(final ACLMessage message) {
        final Class<CustomerObligation> messageClass = customerDraftProtocol.sendClientDecision().getMessageClass();
        final Result<CustomerObligation, DecodingError> decodingResult = codec.decode(message.getContent(), messageClass);
        if (decodingResult.isValid()) {
            draftManagement.registerCustomerObligation(decodingResult.result());
        } else {
            log.error("Could not decode customer obligation", decodingResult.error().getCause());
        }

    }

    private void registerToWhitepages() {
        new ServiceRegistration(this).registerRetryOnFailure(Duration.ofSeconds(3),
                customerDraftProtocol.sendClientDecision().getTargetService(),
                systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().getTargetService());
    }

    private void registerResponseHandler(final String conversationId, final MessageTemplate messageTemplate, final Consumer<ACLMessage> responseHandler) {
        final MessageTemplate templateWithConversationId = MessageTemplate.and(messageTemplate, MessageTemplate.MatchConversationId(conversationId));

        messageHandler.addSpecification(new OneShotMessageSpec(templateWithConversationId, responseHandler));
    }

}
