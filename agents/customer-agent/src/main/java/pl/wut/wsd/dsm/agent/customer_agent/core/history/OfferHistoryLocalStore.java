package pl.wut.wsd.dsm.agent.customer_agent.core.history;

import pl.wut.wsd.dsm.infrastructure.common.function.CollectionTransformer;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class OfferHistoryLocalStore {

    private final List<CustomerOffer> offers = new ArrayList<>();
    private boolean hasCurrent;

    public void registerCurrentOffer(final CustomerOffer offer) {
        hasCurrent = true;
        registerOffers(Collections.singletonList(offer));
    }

    public void registerOffers(final List<CustomerOffer> offers) {
        final List<UUID> newUuids = CollectionTransformer.mapToList(offers, CustomerOffer::getOfferId);
        this.offers.removeIf(o -> newUuids.contains(o.getOfferId()));

        this.offers.addAll(offers);
    }

    public Optional<CustomerOffer> getCurrentOffer() {
        return !hasCurrent ? Optional.empty() : CollectionTransformer.lastElement(offers);
    }

    public Optional<CustomerOffer> findById(final UUID id) {
        return offers.stream().filter(o -> o.getOfferId().equals(id)).findFirst();
    }

    public List<CustomerOffer> getHistory() {
        return new ArrayList<>(offers);
    }
}