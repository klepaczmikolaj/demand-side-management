package pl.wut.wsd.dsm.agent.customerHandler.domain.consumption;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation;
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumption;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ConsumptionEvaluator {

    public double evaluateToPercentageKept(final Customer customer, final List<EnergyConsumption> consumption, final Obligation obligation) {
        return calculateTimeNotKept(customer, obligation, consumption);
    }

    private double calculateTimeNotKept(final Customer customer, final Obligation obligation, final List<EnergyConsumption> consumptions) {
        if (consumptions == null) {
            log.error("Consumptions for obligation {} are null, returning valid, please fix" + obligation.getId());
            return 100;
        }
        final Map<Boolean, List<EnergyConsumption>> isKept = consumptions.stream()
                .collect(Collectors.partitioningBy(c ->
                        obligation.getRelatedOffer().isReduction() ? isReductionKept(c.getWattsConsumption(), obligation, customer) :
                                isIncreaseKept(c.getWattsConsumption(), obligation, customer)));

        return (double) isKept.get(true).size() / (double) (isKept.get(true).size() + isKept.get(false).size()) * 100;
    }

    private boolean isReductionKept(final double consumption, final Obligation obligation, final Customer customer) {
        return consumption <= customer.getNominalUsageInWatts() - obligation.getSizeKws() * 1000;
    }

    private boolean isIncreaseKept(final double consumption, final Obligation obligation, final Customer customer) {
        return consumption > customer.getNominalUsageInWatts() + obligation.getSizeKws() * 1000;
    }
}
