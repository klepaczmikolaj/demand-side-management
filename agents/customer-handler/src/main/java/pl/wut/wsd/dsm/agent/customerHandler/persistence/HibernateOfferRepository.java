package pl.wut.wsd.dsm.agent.customerHandler.persistence;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer_;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class HibernateOfferRepository implements CustomerOfferRepository {

    private final HibernateTemplate hibernateTemplate;

    @Override
    public Optional<Offer> findByOfferId(final UUID offerId) {
        return hibernateTemplate.findOne((r, cb) -> cb.equal(r.get(Offer_.offerId), offerId), Offer.class);
    }

    @Override
    public void saveOffer(final Offer offer) {
        hibernateTemplate.saveOrUpdate(offer);
    }
}
