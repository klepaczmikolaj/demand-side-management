package pl.wut.wsd.dsm.agent.customer_agent.persistence.repo;

import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Offer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerOfferRepository {
    Optional<Offer> findByOfferId(UUID offerId);

    List<Offer> getHistory(final Long customerId);

    List<Offer> getCurrentOffer(final Long customerId);
}
