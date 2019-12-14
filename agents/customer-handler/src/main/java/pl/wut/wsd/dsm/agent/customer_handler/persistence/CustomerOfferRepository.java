package pl.wut.wsd.dsm.agent.customer_handler.persistence;

import pl.wut.wsd.dsm.agent.customer_handler.model.Offer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerOfferRepository {

    Optional<Offer> findByOfferId(UUID offerId);

    void saveOffer(final Offer offer);
}
