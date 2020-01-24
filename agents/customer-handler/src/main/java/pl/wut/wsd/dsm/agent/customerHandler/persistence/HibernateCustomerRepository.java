package pl.wut.wsd.dsm.agent.customerHandler.persistence;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer_;
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
