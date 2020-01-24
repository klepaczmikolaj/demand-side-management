package pl.wut.wsd.dsm.agent.customerHandler.persistence;

import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerOfferRepository {

    Optional<Offer> findByOfferId(UUID offerId);

    void saveOffer(final Offer offer);
}
