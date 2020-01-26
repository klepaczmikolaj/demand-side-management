package pl.wut.wsd.dsm.agent.trust_factor.persistence.repo;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer_;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateCustomerRepository implements CustomerRepository {

    private final HibernateTemplate hibernateTemplate;

    public List<Customer> findAll() {
        return hibernateTemplate.findAll(Customer.class);
    }

    @Override
    public Optional<Customer> findByCustomerId(final Long customerId) {
        return hibernateTemplate.findOne((r, cb) -> cb.equal(r.get(Customer_.customerId), customerId), Customer.class);
    }

    @Override
    public void save(final Customer customer) {
        hibernateTemplate.saveOrUpdate(customer);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return hibernateTemplate.findAll(Customer.class);
    }
}
