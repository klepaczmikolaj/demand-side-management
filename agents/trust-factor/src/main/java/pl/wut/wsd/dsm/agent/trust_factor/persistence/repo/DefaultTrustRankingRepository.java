package pl.wut.wsd.dsm.agent.trust_factor.persistence.repo;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust_;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer_;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class DefaultTrustRankingRepository implements TrustRankingRepository {

    private final HibernateTemplate hibernateTemplate;

    @Override
    public Optional<CustomerTrust> getForCustomer(final Customer customer) {
        return findByCustomerId(customer.getCustomerId());
    }

    @Override
    public Optional<CustomerTrust> findByCustomerId(final Long customerId) {
        return hibernateTemplate.findOne((r, cb) -> cb.equal(r.get(CustomerTrust_.customer).get(Customer_.customerId), customerId), CustomerTrust.class);
    }

    @Override
    public void save(final CustomerTrust customerTrust) {
        hibernateTemplate.saveOrUpdate(customerTrust);
    }
}
