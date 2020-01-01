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

    public Map<Long, CustomerOffer> prepareCustomerOffers(final List<CustomerTrustRankingEntry> ranking, final ExpectedInbalancement expectedInbalancement) {
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
}
