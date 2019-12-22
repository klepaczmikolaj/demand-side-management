package pl.wut.wsd.dsm.agent.customer_agent.core;

import jade.lang.acl.ACLMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_agent.core.history.ObigationHistoryLocalStore;
import pl.wut.wsd.dsm.agent.customer_agent.core.history.OfferHistoryLocalStore;
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
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CustomerObligationService implements ObligationsService, OffersService {

    private final AgentMessagingCapability messages;
    private final Codec codec;
    private final Customer customer;
    private final ApiTypesMapper mapper;
    private final CustomerDraftProtocol.AcceptClientDecision acceptClientDecision;
    private final ObigationHistoryLocalStore obligations;
    private final OfferHistoryLocalStore offers;

    @Override
    public Optional<ObligationRepresentation> getCurrentObligation() {
        return obligations.getCurrentObligation().map(o -> mapper.toRepresentation(o, offers.findById(o.getRelatedOfferId()).get()));
    }

    @Override
    public List<ObligationRepresentation> getObligationHistory() {
        return CollectionTransformer.mapToList(obligations.getHistory(), o -> mapper.toRepresentation(o, offers.findById(o.getRelatedOfferId()).get()));
    }

    @Override
    public Result<ObligationRepresentation, ApiError> acceptObligation(final ObligationAcceptanceRequest request) {
        final Optional<CustomerOffer> maybeOffer = offers.findById(request.getRelatedOfferId());

        if (maybeOffer.isPresent()) {
            final CustomerOffer relatedOffer = maybeOffer.get();
            if (relatedOffer.getValidUntil().isAfter(ZonedDateTime.now())) {

                final ACLMessage message = acceptClientDecision.templatedMessage();
                final CustomerObligation customerObligation = mapper.toObligation(request, relatedOffer);
                message.setContent(codec.encode(customerObligation));
                messages.send(message, acceptClientDecision.serviceDescription(customer));

                return Result.ok(mapper.toRepresentation(customerObligation, relatedOffer));
            } else {
                return Result.error(ApiError.badRequest("Offer has expired"));
            }
        } else {
            return Result.error(ApiError.badRequest("No offer found"));
        }

    }

    @Override
    public Optional<CustomerOfferRepresentation> getCurrentOffer() {
        return offers.getCurrentOffer().map(mapper::toRepresentation);
    }

    @Override
    public List<CustomerOfferRepresentation> getOfferHistory() {
        return CollectionTransformer.mapToList(offers.getHistory(), mapper::toRepresentation);
    }

    @Override
    public void registerOffer(final CustomerOffer offer) {
        offers.registerCurrentOffer(offer);
    }
}
