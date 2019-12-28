package pl.wut.wsd.dsm.agent.customer_handler.persistence;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.customer_handler.model.Obligation;
import pl.wut.wsd.dsm.agent.customer_handler.model.Obligation_;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class HibernateCustomerObligationRepository implements CustomerObligationRepository {

    private final HibernateTemplate hibernateTemplate;

    @Override
    public List<Obligation> getCustomerObligations(final UUID customerId) {
        return hibernateTemplate.findAll((r, cb) -> cb.equal(r.get(Obligation_.customerId), customerId), Obligation.class);
    }

    @Override
    public void saveOrUpdate(final Obligation obligation) {
        hibernateTemplate.saveOrUpdate(obligation);
    }

}
