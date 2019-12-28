package pl.wut.wsd.dsm.agent.trust_factor.ranking;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerObligationState;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TrustRankingComputer {

    private static final double successCoefficent = 1;
    private static final double failureCoefficent = 3;

    private static final double minValue = 0;
    private static final double maxValue = 5;

    public TrustComputationResult compute(final CustomerTrust customerTrust, final Set<Obligation> obligationsToProcess) {
        final Map<CustomerObligationState, List<Obligation>> states = obligationsToProcess.stream()
                .collect(Collectors.groupingBy(Obligation::getState));
        final List<Obligation> failedObligations = states.getOrDefault(CustomerObligationState.FAILED, Collections.emptyList());
        final List<Obligation> obligationsSuccedeed = states.getOrDefault(CustomerObligationState.KEPT, Collections.emptyList());

        final double failedObligationsWeight = failedObligations.stream()
                .mapToDouble(obligation -> (1 - (obligation.getPerecentageKept())) * obligation.getSizeKws())
                .sum();

        final double obligationsSucceededWeight = obligationsSuccedeed.stream()
                .mapToDouble(Obligation::getSizeKws)
                .sum();

        final Double currentValue = customerTrust.getCurrentValue();
        final Double kwsProcessed = customerTrust.getKwsProcessed();

        final double newValue = computeNewValue(currentValue, kwsProcessed, obligationsSucceededWeight, failedObligationsWeight);

        return new TrustComputationResult(newValue, kwsProcessed + obligationsToProcess.stream().mapToDouble(Obligation::getSizeKws).sum());
    }

    private double computeNewValue(final Double currentValue, final Double kwsProcessedSoFar, final double obligationsSucceededWeight, final double failedObligationsWeight) {
        final double keptSoFarWeight = kwsProcessedSoFar * (currentValue) / (maxValue - minValue) * successCoefficent;
        final double failedSoFarWeight = kwsProcessedSoFar * (maxValue - currentValue) / (maxValue - minValue) * failureCoefficent;

        final double keptWeight = obligationsSucceededWeight + keptSoFarWeight;
        final double failedWeight = failedObligationsWeight + failedSoFarWeight;

        return maxValue - (maxValue - minValue) * failedWeight / (keptWeight + failedWeight);
    }

    @ToString
    @EqualsAndHashCode
    static class TrustComputationResult {
        final double newValue;
        final double processed;

        public TrustComputationResult(final double newValue, final double processed) {
            this.newValue = newValue;
            this.processed = processed;
        }
    }
}
