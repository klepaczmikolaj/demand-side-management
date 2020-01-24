package pl.wut.wsd.dsm.agent.customerHandler.mapper;

import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.ontology.draft.EnergyConsumptionChange;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class CustomerHandlerTypesMapper {

    public CustomerOffer mapToDto(final Offer offer) {
        final CustomerOffer dto = new CustomerOffer();
        dto.setOfferId(offer.getOfferId());
        dto.setEnergyConsumptionChange(new EnergyConsumptionChange(offer.getKws(), offer.getDemandChangeSince(), offer.getDemandChangeUntil()));
        dto.setValidUntil(offer.getValidUntil());
        dto.setPricePerKw(offer.getPricePerKw());

        return dto;
    }

    public Offer mapToEntity(final CustomerOffer customerOffer, final Customer customer) {
        final EnergyConsumptionChange change = customerOffer.getEnergyConsumptionChange();
        final ZonedDateTime validUntil = customerOffer.getValidUntil();
        final UUID offerId = customerOffer.getOfferId();
        final BigDecimal pricePerKw = customerOffer.getPricePerKw();

        return customerOffer.getType() == ObligationType.REDUCTION ?
                Offer.reduction(offerId, customer, validUntil, change.getAvailKws(), pricePerKw, change.getSince(), change.getUntil()) :
                Offer.increase(offerId, customer, validUntil, change.getAvailKws(), pricePerKw, change.getSince(), change.getUntil());
    }

}
