package pl.wut.wsd.dsm.agent.quote_manager.offer;

import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.ontology.draft.EnergyConsumptionChange;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRankingEntry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OfferPreparer {

    private static final double nominalElectricityPrice = 0.5;
    private static final int wattsInKw = 1000;

    public Map<Long, CustomerOffer> prepareCustomerOffers(final List<CustomerTrustRankingEntry> ranking, final ExpectedInbalancement expectedInbalancement) {
        return ranking.stream().collect(Collectors.toMap(e -> e.getCustomer().getCustomerId(), e -> {
            final double wattsDemand = expectedInbalancement.getExpectedDemandAndProduction().getWattsDemand();
            final double wattsProduction = expectedInbalancement.getExpectedDemandAndProduction().getWattsProduction();
            final CustomerOffer customerOffer = new CustomerOffer();
            customerOffer.setOfferId(UUID.randomUUID());
            double inbalanceAbsoluteValue  = Math.abs(expectedInbalancement.getExpectedDemandAndProduction().getWattsDemand() - expectedInbalancement.getExpectedDemandAndProduction().getWattsProduction());
            customerOffer.setPricePerKw(BigDecimal.valueOf(nominalElectricityPrice * inbalanceAbsoluteValue/5000));
            customerOffer.setValidUntil(expectedInbalancement.getSince());
            if (wattsDemand > wattsProduction) {
                customerOffer.setType(ObligationType.REDUCTION);
            } else {
                customerOffer.setType(ObligationType.INCREASE);
            }
            customerOffer.setEnergyConsumptionChange(
                    new EnergyConsumptionChange(Math.abs(wattsToKw(wattsDemand - wattsProduction)) / ranking.size(), expectedInbalancement.getSince(), expectedInbalancement.getUntil())
            );
            return customerOffer;
        }));
    }

    private double wattsToKw(final double watts) {
        return watts / wattsInKw;
    }
}
