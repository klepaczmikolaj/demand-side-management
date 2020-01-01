package pl.wut.wsd.dsm.agent.quote_manager;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.quote_manager.draft.DraftManagement;
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
import pl.wut.wsd.dsm.ontology.draft.EnergyConsumptionChange;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRankingEntry;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.protocol.SystemDraftProtocol;
import pl.wut.wsd.dsm.protocol.customer_trust.GetCustomerTrustProtocol;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    @Override
    protected void setup() {
        final QuoteAgentDependencies dependencies = (QuoteAgentDependencies) getArguments()[0];
        this.codec = dependencies.codec();
        this.addBehaviour(messageHandler);
        registerToWhitepages();
    }

    private void processInbalancement(final ACLMessage aclMessage) {
        if (draftManagement.currentDraftStillInProgress()) {
            log.info("Current draft is still in progress, new draft wont be started, current: {}", draftManagement.getSummaryStatistics());
            return;
        }

        final Class<ExpectedInbalancement> messageClass = systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().getMessageClass();
        final Result<ExpectedInbalancement, DecodingError> decodingResult = codec.decode(aclMessage.getContent(), messageClass);

        if (!decodingResult.isValid()) {
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
            final Map<Long, CustomerOffer> offerByCustomerId = prepareCustomerOffers(ranking, expectedInbalancement);

            draftManagement.registerClientOffers(new HashSet<>(offerByCustomerId.values()));

            offerByCustomerId.forEach(this::sendCustomerOffer);
        }
    }

    private void sendCustomerOffer(final Long customerId, final CustomerOffer customerOffer) {
        final ServiceDescription customerService = customerDraftProtocol.sendOfferToHandler().serviceDescription(new Customer(customerId));
        final Result<List<DFAgentDescription>, FIPAException> searchResult = serviceDiscovery.findServices(customerService);
        if (searchResult.isValid()) {
            if (!searchResult.result().isEmpty()) {
                final ACLMessage message = customerDraftProtocol.sendOfferToHandler().templatedMessage();
                message.setConversationId(UUID.randomUUID().toString());
                message.setContent(codec.encode(customerOffer));
                message.addReceiver(searchResult.result().get(0).getName());
                registerResponseHandler(message.getConversationId(), customerDraftProtocol.sendClientDecision().toMessageTemplate(), this::processClientResponse);
                send(message);
                log.info("Customer offer sent");
            }
        } else {
            log.error("Could not send customer offer to customer {}, because {}", customerId, searchResult.error().getMessage());
        }
    }

    private Map<Long, CustomerOffer> prepareCustomerOffers(final List<CustomerTrustRankingEntry> ranking, final ExpectedInbalancement expectedInbalancement) {
        return ranking.stream().collect(Collectors.toMap(e -> e.getCustomer().getCustomerId(), e -> {
            final double wattsDemand = expectedInbalancement.getExpectedDemandAndProduction().getWattsDemand();
            final double wattsProduction = expectedInbalancement.getExpectedDemandAndProduction().getWattsProduction();
            final CustomerOffer customerOffer = new CustomerOffer();
            customerOffer.setOfferId(UUID.randomUUID());
            customerOffer.setPricePerKw(BigDecimal.ONE);
            customerOffer.setValidUntil(expectedInbalancement.getSince());
            if (wattsDemand > wattsProduction) {
                customerOffer.setType(ObligationType.INCREASE);
            } else {
                customerOffer.setType(ObligationType.REDUCTION);
            }
            customerOffer.setEnergyConsumptionChange(
                    new EnergyConsumptionChange(Math.abs((wattsDemand - wattsProduction)) / ranking.size(), expectedInbalancement.getSince(), expectedInbalancement.getUntil())
            );
            return customerOffer;
        }));
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
