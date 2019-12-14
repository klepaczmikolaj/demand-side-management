package pl.wut.wsd.dsm.agent.customer_handler.persistence;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.customer_handler.model.Offer;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class HibernateOfferRepository implements CustomerOfferRepository {

    private final HibernateTemplate hibernateTemplate;

    @Override
    public Optional<Offer> findByOfferId(final Long offerId) {
        return null;
    }

    @Override
    public void saveOffer(final Offer offer) {
        hibernateTemplate.saveOrUpdate(offer);
    }
}
