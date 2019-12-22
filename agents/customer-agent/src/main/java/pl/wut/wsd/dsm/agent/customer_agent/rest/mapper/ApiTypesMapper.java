package pl.wut.wsd.dsm.agent.customer_agent.rest.mapper;

import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.ontology.draft.EnergyConsumptionChange;

import java.math.BigDecimal;

public class ApiTypesMapper {

    public CustomerOfferRepresentation toRepresentation(final CustomerOffer offer) {
        final CustomerOfferRepresentation representation = new CustomerOfferRepresentation();
        representation.setOfferId(offer.getOfferId());
        representation.setOfferEndDateTime(offer.getValidUntil());
        final EnergyConsumptionChange increase = offer.getEnergyConsumptionChange();
        representation.setSizeKws(increase.getAvailKws());
        representation.setDemandChangeStart(increase.getSince());
        representation.setDemandChangeEnd(increase.getUntil());
        representation.setType(offer.getType());

        return representation;
    }

    public CustomerOffer toOffer(final CustomerOfferRepresentation dto) {
        final CustomerOffer offer = new CustomerOffer();
        offer.setOfferId(dto.getOfferId());
        offer.setPricePerKw(BigDecimal.valueOf(dto.getAmountPerKWh()));
        offer.setValidUntil(dto.getOfferEndDateTime());
        offer.setType(dto.getType());
        offer.setEnergyConsumptionChange(new EnergyConsumptionChange(dto.getSizeKws(), dto.getDemandChangeStart(), dto.getOfferEndDateTime()));

        return offer;
    }

    public CustomerObligation toObligation(final ObligationAcceptanceRequest request, final CustomerOffer relatedOffer) {
        final CustomerObligation obligation = new CustomerObligation();
        obligation.setRelatedOfferId(relatedOffer.getOfferId());
        obligation.setKwsChange(request.getKwsAccepted());
        obligation.setObligationType(relatedOffer.getType());

        return obligation;
    }

    public ObligationRepresentation toRepresentation(final CustomerObligation obligation, final CustomerOffer offer) {
        final ObligationRepresentation representation = new ObligationRepresentation();
        representation.setRelatedOfferId(obligation.getRelatedOfferId());
        representation.setObligationSizeKw(obligation.getKwsChange());
        representation.setSince(offer.getEnergyConsumptionChange().getSince());
        representation.setUntil(offer.getEnergyConsumptionChange().getUntil());
        representation.setType(offer.getType());

        return representation;
    }

}
