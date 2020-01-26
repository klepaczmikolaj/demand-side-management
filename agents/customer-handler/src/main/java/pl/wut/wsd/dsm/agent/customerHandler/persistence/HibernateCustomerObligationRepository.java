package pl.wut.wsd.dsm.agent.customerHandler.persistence;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.CustomerObligationState;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer_;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation_;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer_;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class HibernateCustomerObligationRepository implements CustomerObligationRepository {

    private final HibernateTemplate hibernateTemplate;

    @Override
    public List<Obligation> getCustomerObligations(final Long customerId) {
        return hibernateTemplate.findAll((r, cb) -> cb.equal(r.get(Obligation_.customer).get(Customer_.customerId), customerId), Obligation.class);
    }

    @Override
    public List<Obligation> getAllUnresolved(final Long customerId) {
        return hibernateTemplate.findAll((r, cb) -> {
            final Predicate customerIdMatches = cb.equal(r.get(Obligation_.customer).get(Customer_.customerId), customerId);
            final Predicate notResolved = cb.not(r.get(Obligation_.state).in(CustomerObligationState.RESOLVED));

            return cb.and(customerIdMatches, notResolved);
        }, Obligation.class);
    }

    @Override
    public Optional<Obligation> getById(final Long id) {
        return hibernateTemplate.getOne(id, Obligation.class);
    }

    @Override
    public Optional<Obligation> findByRelatedOfferId(final UUID relatedOfferId) {
        return hibernateTemplate.findOne((r, cb) -> cb.equal(r.get(Obligation_.relatedOffer).get(Offer_.offerId), relatedOfferId), Obligation.class);
    }

    @Override
    public boolean isResolved(final Long obligationId) {
        return hibernateTemplate.getOne(obligationId, Obligation.class).map(Obligation::getState)
                .filter(CustomerObligationState.RESOLVED::contains)
                .isPresent();
    }

    @Override
    public void saveOrUpdate(final Obligation obligation) {
        hibernateTemplate.saveOrUpdate(obligation);
    }

}
