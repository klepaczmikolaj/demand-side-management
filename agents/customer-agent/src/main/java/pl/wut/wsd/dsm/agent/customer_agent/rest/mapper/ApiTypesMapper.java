package pl.wut.wsd.dsm.agent.customer_agent.rest.mapper;

import lombok.NonNull;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Obligation;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Offer;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;

import java.time.ZonedDateTime;

public class ApiTypesMapper {

    public CustomerOfferRepresentation toRepresentation(final Offer offer) {
        final CustomerOfferRepresentation representation = new CustomerOfferRepresentation();
        representation.setOfferId(offer.getOfferId());
        representation.setOfferEndDateTime(offer.getValidUntil().toLocalDateTime());
        representation.setSizeKws(offer.getKws());
        representation.setDemandChangeStart(offer.getDemandChangeSince().toLocalDateTime());
        representation.setDemandChangeEnd(offer.getDemandChangeUntil().toLocalDateTime());
        representation.setType(mapType(offer.getType()));
        representation.setAmountPerKWh(offer.getPricePerKw().doubleValue());
        representation.setAvailable(offer.getObligation() == null && offer.getDemandChangeSince().isAfter(ZonedDateTime.now()));
        return representation;
    }

    public CustomerObligation toObligation(final ObligationAcceptanceRequest request, final Offer relatedOffer) {
        final CustomerObligation obligation = new CustomerObligation();
        obligation.setRelatedOfferId(relatedOffer.getOfferId());
        obligation.setKwsChange(request.getKwsAccepted());
        obligation.setObligationType(mapType(relatedOffer.getType()));

        return obligation;
    }

    public ObligationRepresentation toRepresentation(final Obligation obligation) {
        @NonNull final Offer offer = obligation.getRelatedOffer();
        final ObligationRepresentation representation = new ObligationRepresentation();
        representation.setRelatedOfferId(offer.getOfferId());
        representation.setObligationSizeKw(obligation.getSizeKws());
        representation.setSince(offer.getDemandChangeSince());
        representation.setUntil(offer.getValidUntil());
        representation.setType(offer.getType() == Offer.Type.REDUCTION ? ObligationType.REDUCTION : ObligationType.INCREASE);
        if (offer.isReduction()) {
            final double notAboveWatts = offer.getCustomer().getNominalUsageInWatts() - 1000 * obligation.getSizeKws();
            representation.setNotExceedingKws(notAboveWatts / 1000);
        } else {
            final double notBelowWatts = offer.getCustomer().getNominalUsageInWatts() + 1000 * obligation.getSizeKws();
            representation.setNotBelowKws(notBelowWatts / 1000);
        }
        return representation;
    }

    private ObligationType mapType(final Offer.Type type) {
        return type == Offer.Type.REDUCTION ? ObligationType.REDUCTION : ObligationType.INCREASE;
    }

    public ObligationRepresentation toRepresentation(final ObligationAcceptanceRequest request,
                                                     final Offer relatedOffer) {
        final ObligationRepresentation representation = new ObligationRepresentation();
        representation.setRelatedOfferId(relatedOffer.getOfferId());
        representation.setObligationSizeKw(request.getKwsAccepted());
        representation.setSince(relatedOffer.getDemandChangeSince());
        representation.setUntil(relatedOffer.getValidUntil());
        final ObligationType obligationType = mapType(relatedOffer.getType());
        representation.setType(obligationType);
        if (obligationType == ObligationType.REDUCTION) {
            final double notAboveWatts = relatedOffer.getCustomer().getNominalUsageInWatts() - 1000 * request.getKwsAccepted();
            representation.setNotExceedingKws(notAboveWatts / 1000);
        } else {
            final double notBelowWatts = relatedOffer.getCustomer().getNominalUsageInWatts() + 1000 * request.getKwsAccepted();
            representation.setNotBelowKws(notBelowWatts / 1000);
        }

        return representation;
    }
}
