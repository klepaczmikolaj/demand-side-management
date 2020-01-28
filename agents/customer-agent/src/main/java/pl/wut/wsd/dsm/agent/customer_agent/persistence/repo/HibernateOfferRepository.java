package pl.wut.wsd.dsm.agent.customer_agent.persistence.repo;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Customer_;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Offer;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Offer_;
import pl.wut.wsd.dsm.infrastructure.persistence.Specification;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class HibernateOfferRepository implements CustomerOfferRepository {

    private final HibernateTemplate hibernateTemplate;

    private Specification<Offer> offersForUser(final Long customerId) {
        return (r, cb) -> cb.equal(r.get(Offer_.customer).get(Customer_.customerId), customerId);
    }

    @Override
    public Optional<Offer> findByOfferId(final UUID offerId) {
        return hibernateTemplate.findOne((r, cb) -> cb.equal(r.get(Offer_.offerId), offerId), Offer.class);
    }

    @Override
    public List<Offer> getHistory(final Long customerId) {
        return hibernateTemplate.findAll(offersForUser(customerId), Offer.class);
    }

    @Override
    public List<Offer> getCurrentOffer(final Long customerId) {
        return hibernateTemplate.findAll((r, cb) -> cb.and(
                r.get(Offer_.state).in(Offer.State.PENDING),
                cb.greaterThan(r.get(Offer_.validUntil), ZonedDateTime.now())
        ), Offer.class);
    }
}
