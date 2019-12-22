package pl.wut.wsd.dsm.agent.customer_agent.core.history;

import pl.wut.wsd.dsm.infrastructure.common.function.CollectionTransformer;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ObigationHistoryLocalStore {

    private final List<CustomerObligation> obligations = new ArrayList<>();
    private boolean hasCurrent;

    public void registerCurrentObligation(final CustomerObligation obligation) {
        hasCurrent = true;
        registerObligations(Collections.singletonList(obligation));
    }

    public void registerObligations(final List<CustomerObligation> obligations) {
        final List<UUID> newUuids = CollectionTransformer.mapToList(obligations, CustomerObligation::getRelatedOfferId);
        this.obligations.removeIf(o -> newUuids.contains(o.getRelatedOfferId()));

        this.obligations.addAll(obligations);
    }

    public Optional<CustomerObligation> getCurrentObligation() {
        return hasCurrent ? Optional.empty() : CollectionTransformer.lastElement(obligations);
    }

    public List<CustomerObligation> getHistory() {
        return new ArrayList<>(obligations);
    }
}
