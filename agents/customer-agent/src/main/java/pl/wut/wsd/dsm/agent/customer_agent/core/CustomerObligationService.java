package pl.wut.wsd.dsm.agent.customer_agent.core;

import pl.wut.wsd.dsm.agent.customer_agent.rest.model.ApiError;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerObligationService implements ObligationsService, OffersService {

    private boolean hasCurrentObligation;
    private final List<ObligationRepresentation> obligations = new ArrayList<>();
    private final List<CustomerOfferRepresentation> offers = new ArrayList<>();

    @Override
    public Optional<ObligationRepresentation> getCurrentObligation() {

        return hasCurrentObligation ? Optional.of(obligations.get(obligations.size() - 1)) : Optional.empty();
    }

    @Override
    public List<ObligationRepresentation> getObligationHistory() {
        return new ArrayList<>(obligations);
    }

    @Override
    public Result<ObligationRepresentation, ApiError> acceptObligation(final ObligationAcceptanceRequest request) {
        final Optional<CustomerOfferRepresentation> offer = offers.stream()
                .filter(o -> o.getOfferId().equals(request.getRelatedOfferId()))
                .findFirst();

        if (offer.isPresent()) {
            if (offer.get().getOfferEndDateTime().isAfter(ZonedDateTime.now())) {
                return Result.ok(addObligation(request, offer.get()));
            } else {
                return Result.error(ApiError.badRequest("Offer has expired"));
            }
        } else {
            return Result.error(ApiError.badRequest("No offer found"));
        }

    }

    private ObligationRepresentation addObligation(final ObligationAcceptanceRequest request, final CustomerOfferRepresentation offer) {
        final ObligationRepresentation representation = new ObligationRepresentation();
        representation.setAvailable(true);
        representation.setObligationSizeKw(request.getKwsAccepted());
        representation.setRelatedOfferId(offer.getOfferId());
        representation.setSince(offer.getDemandChangeStart());
        representation.setUntil(offer.getDemandChangeEnd());
        obligations.add(representation);
        hasCurrentObligation = true;

        return representation;
    }

    @Override
    public Optional<CustomerOfferRepresentation> getCurrentOffer() {
        if (!offers.isEmpty() && offers.get(offers.size() - 1).getOfferEndDateTime().isAfter(ZonedDateTime.now())) {
            return Optional.of(offers.get(offers.size() - 1));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<CustomerOfferRepresentation> getOfferHistory() {
        return new ArrayList<>(offers);
    }

    @Override
    public void registerOffer(final CustomerOffer offer) {
        final CustomerOfferRepresentation representation = new CustomerOfferRepresentation();
        representation.setAmountPerKWh(offer.getPricePerKw().doubleValue());
        Optional.ofNullable(offer.getEnergyConsumptionReduction()).ifPresent(r -> {
            representation.setReducedKws(r.getSizeKws());
            representation.setDemandChangeStart(r.getSince());
            representation.setDemandChangeEnd(r.getUntil());
        });
        Optional.ofNullable(offer.getEnergyConsumptionIncrease()).ifPresent(i -> {
            representation.setReducedKws(i.getAvailKws());
            representation.setDemandChangeStart(i.getSince());
            representation.setDemandChangeEnd(i.getUntil());
        });
        representation.setOfferEndDateTime(offer.getValidUntil());
        offers.add(representation);
    }
}
