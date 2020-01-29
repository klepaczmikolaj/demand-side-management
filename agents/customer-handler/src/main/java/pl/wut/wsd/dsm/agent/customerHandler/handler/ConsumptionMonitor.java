package pl.wut.wsd.dsm.agent.customerHandler.handler;

import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.customerHandler.domain.consumption.ConsumptionEvaluator;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.common.function.CollectionTransformer;
import pl.wut.wsd.dsm.infrastructure.handle.ParsingHandler;
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumption;
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumptionProtocol;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ConsumptionMonitor extends ParsingHandler<EnergyConsumption, EnergyConsumptionProtocol.ConsumptionInformation> {

    private final CustomerObligationRepository repo;
    private final Long customerId;
    private final Map<Long, List<EnergyConsumption>> consumptionEntries = new HashMap<>();
    private final ConsumptionEvaluator evaluator;

    public ConsumptionMonitor(final Codec codec,
                              final EnergyConsumptionProtocol.ConsumptionInformation protocolStep,
                              final CustomerObligationRepository repo,
                              final Long customerId) {
        super(codec, protocolStep);
        this.repo = repo;
        this.customerId = customerId;
        this.evaluator = new ConsumptionEvaluator();
    }

    @Override
    protected void handle(final EnergyConsumption energyConsumption) {
        final List<Obligation> obligations = repo.getAllUnresolved(customerId);
        final Map<Boolean, List<Obligation>> finished = obligations.stream().collect(Collectors.partitioningBy(this::isFinished));

        finished.get(false).stream().map(Obligation::getId).forEach(id -> consumptionEntries.merge(id, Collections.singletonList(energyConsumption), CollectionTransformer::merge));
        finished.get(true).forEach(this::performObligationEvaluation);
    }

    private void performObligationEvaluation(final Obligation obligation) {
        log.info("Performing obligation evaluation for obligation {}", obligation.getId());
        final List<EnergyConsumption> consumptions = consumptionEntries.get(obligation.getId());
        final double kept = evaluator.evaluateToPercentageKept(obligation.getCustomer(), consumptions, obligation);
        obligation.setPercentageKept(kept);

        repo.saveOrUpdate(obligation);
    }

    private boolean isFinished(final Obligation obligation) {
        return obligation.getRelatedOffer().getDemandChangeUntil().isBefore(ZonedDateTime.now());
    }


}
