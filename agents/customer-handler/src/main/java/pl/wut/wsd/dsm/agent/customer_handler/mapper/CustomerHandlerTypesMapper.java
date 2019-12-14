package pl.wut.wsd.dsm.agent.customer_handler.mapper;

import pl.wut.wsd.dsm.agent.customer_handler.model.Offer;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.ontology.draft.EnergyConsumptionIncrease;
import pl.wut.wsd.dsm.ontology.draft.EnergyConsumptionReduction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class CustomerHandlerTypesMapper {

    public CustomerOffer mapToDto(final Offer offer) {
        final CustomerOffer dto = new CustomerOffer();
        dto.setOfferId(offer.getOfferId());
        if (offer.getType() == Offer.Type.REDUCTION) {
            dto.setEnergyConsumptionReduction(new EnergyConsumptionReduction(offer.getKws(), offer.getDemandChangeSince(), offer.getDemandChangeUntil()));
        } else {
            dto.setEnergyConsumptionIncrease(new EnergyConsumptionIncrease(offer.getKws(), offer.getDemandChangeSince(), offer.getDemandChangeUntil()));
        }
        dto.setValidUntil(offer.getValidUntil());
        dto.setPricePerKw(offer.getPricePerKw());

        return dto;
    }

    public Offer mapToEntity(final CustomerOffer customerOffer, final Long customerId) {
        final EnergyConsumptionReduction reduction = customerOffer.getEnergyConsumptionReduction();
        final EnergyConsumptionIncrease increase = customerOffer.getEnergyConsumptionIncrease();
        final ZonedDateTime validUntil = customerOffer.getValidUntil();
        final UUID offerId = customerOffer.getOfferId();
        final BigDecimal pricePerKw = customerOffer.getPricePerKw();

        return increase == null ?
                Offer.reduction(offerId, customerId, validUntil, reduction.getSizeKws(), pricePerKw, reduction.getSince(), reduction.getUntil()) :
                Offer.increase(offerId, customerId, validUntil, increase.getAvailKws(), pricePerKw, increase.getSince(), increase.getUntil());
    }
}
