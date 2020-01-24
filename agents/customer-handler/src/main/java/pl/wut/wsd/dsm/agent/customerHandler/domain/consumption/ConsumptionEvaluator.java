package pl.wut.wsd.dsm.agent.customerHandler.domain.consumption;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerRepository;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumption;

import java.util.List;

@RequiredArgsConstructor
public class ConsumptionEvaluator {

    private final CustomerRepository customerRepository;
    private final CustomerObligationRepository repository;

    public double evaluateToPercentageKept(final Customer customer, final List<EnergyConsumption> consumption, final CustomerObligation obligation) {
        final int nominalWattsUsage = customer.getNominalUsageInWatts();
        repository.findByRelatedOfferId(obligation.getRelatedOfferId());

        consumption.get(0);

        return 0;
    }

//    private double calculateTimeNotKept() {
//
//    }

//    private double calculateUsageNotKept() {
//
//    }
}
