package pl.wut.wsd.dsm.agent.customer_agent.persistence.repo;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Customer;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Customer_;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class HibernateCustomerRepository implements CustomerRepository {

    private final HibernateTemplate hibernateTemplate;

    @Override
    public Optional<Customer> findByCustomerId(final Long customerId) {
        return hibernateTemplate.findOne((r, cb) -> cb.equal(r.get(Customer_.customerId), customerId), Customer.class);
    }

    @Override
    public void save(final Customer customer) {
        hibernateTemplate.saveOrUpdate(customer);
    }
}
