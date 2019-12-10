package pl.wut.wsd.dsm.agent.quote_manager;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.infrastructure.messaging.OneShotMessageSpec;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.ontology.draft.EnergyConsumptionIncrease;
import pl.wut.wsd.dsm.ontology.draft.EnergyConsumptionReduction;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRankingEntry;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.protocol.SystemDraftProtocol;
import pl.wut.wsd.dsm.protocol.customer_trust.GetCustomerTrustProtocol;

import java.math.BigDecimal;
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
            MessageSpecification.of(systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().toMessageTemplate(), this::processNewDraft)
    ));

    @Override
    protected void setup() {
        final QuoteAgentDependencies dependencies = (QuoteAgentDependencies) getArguments()[0];
        this.codec = dependencies.codec();
        this.addBehaviour(messageHandler);
        registerToWhitepages();
    }

    private void processNewDraft(final ACLMessage aclMessage) {
        final Class<ExpectedInbalancement> messageClass = systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().getMessageClass();
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

                registerResponseHandler(trustRequest.getConversationId(), GetCustomerTrustProtocol.customerTrustRankingResponse.toMessageTemplate(), message -> continueProcessAfterTrustResponse(message, inbalancement));
            }
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

            offerByCustomerId.forEach(this::SendCustomerOffer);
        }
    }

    private void SendCustomerOffer(final Long customerId, final CustomerOffer customerOffer) {
        final ServiceDescription customerService = customerDraftProtocol.sendClientOffer().serviceDescription(new Customer(customerId));
        final Result<List<DFAgentDescription>, FIPAException> searchResult = serviceDiscovery.findServices(customerService);
        if (searchResult.isValid()) {
            final ACLMessage message = customerDraftProtocol.sendClientOffer().templatedMessage();
            message.setConversationId(UUID.randomUUID().toString());
            message.setContent(codec.encode(customerOffer));
            send(message);
            registerResponseHandler(message.getConversationId(),
                    customerDraftProtocol.sendClientDecision().toMessageTemplate(),
                    this::processClientResponse);
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
                customerOffer.setEnergyConsumptionIncrease(
                        new EnergyConsumptionIncrease((wattsDemand - wattsProduction) / ranking.size(), expectedInbalancement.getSince(), expectedInbalancement.getUntil())
                );
            } else {
                customerOffer.setEnergyConsumptionReduction(
                        new EnergyConsumptionReduction((wattsProduction - wattsDemand) / ranking.size(), expectedInbalancement.getSince(), expectedInbalancement.getUntil())
                );
            }
            return customerOffer;
        }));
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

    private void registerResponseHandler(final String conversationId, final MessageTemplate messageTemplate, final Consumer<ACLMessage> responseHandler) {
        final MessageTemplate templateWithConversationId = MessageTemplate.and(messageTemplate, MessageTemplate.MatchConversationId(conversationId));

        messageHandler.addSpecification(new OneShotMessageSpec(templateWithConversationId, responseHandler));
    }
}
