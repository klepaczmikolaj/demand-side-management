package pl.wut.wsd.dsm.agent.customer_agent.core;

import jade.core.AID;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Offer;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.repo.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.repo.CustomerOfferRepository;
import pl.wut.wsd.dsm.agent.customer_agent.rest.mapper.ApiTypesMapper;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.ApiError;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.common.function.CollectionTransformer;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.messaging.handle.AgentMessagingCapability;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class CustomerObligationService implements ObligationsService, OffersService {

    private final AgentMessagingCapability messages;
    private final Codec codec;
    private final Customer customer;
    private final ApiTypesMapper mapper;
    private final CustomerDraftProtocol.AcceptClientDecision acceptClientDecision;
    private final CustomerObligationRepository obligationRepository;
    private final CustomerOfferRepository offerRepository;

    @Override
    public List<ObligationRepresentation> getCurrentObligations() {
        return CollectionTransformer.mapToList(obligationRepository.getAllUnresolved(customer.getCustomerId()), mapper::toRepresentation);
    }

    @Override
    public List<ObligationRepresentation> getObligationHistory() {
        return CollectionTransformer.mapToList(obligationRepository.findAll(), mapper::toRepresentation);
    }

    @Override
    public Result<ObligationRepresentation, ApiError> acceptObligation(final ObligationAcceptanceRequest request) {
        final Optional<Offer> maybeOffer = offerRepository.findByOfferId(request.getRelatedOfferId());

        if (maybeOffer.isPresent()) {
            final Offer relatedOffer = maybeOffer.get();
            if (relatedOffer.getValidUntil().isAfter(ZonedDateTime.now())) {

                final ACLMessage message = acceptClientDecision.templatedMessage();
                final CustomerObligation customerObligation = mapper.toObligation(request, relatedOffer);
                message.setContent(codec.encode(customerObligation));
                final Result<Set<AID>, FIPAException> sendResult = messages.send(message, acceptClientDecision.serviceDescription(customer));
                return sendResult.isValid() ? Result.ok(mapper.toRepresentation(request, relatedOffer)) : Result.error(ApiError.internal(sendResult.error().getMessage()));
            } else {
                return Result.error(ApiError.badRequest("Offer has expired"));
            }
        } else {
            return Result.error(ApiError.badRequest("No offer found"));
        }

    }

    @Override
    public List<CustomerOfferRepresentation> getPendingOffers() {
        return CollectionTransformer.mapToList(offerRepository.getCurrentOffer(customer.getCustomerId()), mapper::toRepresentation);
    }

    @Override
    public List<CustomerOfferRepresentation> getOfferHistory() {
        return CollectionTransformer.mapToList(offerRepository.getHistory(customer.getCustomerId()), mapper::toRepresentation);
    }

}
