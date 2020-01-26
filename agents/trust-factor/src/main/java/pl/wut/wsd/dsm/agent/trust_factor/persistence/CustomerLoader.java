package pl.wut.wsd.dsm.agent.trust_factor.persistence;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.util.List;

@RequiredArgsConstructor
public class CustomerLoader {

    private final HibernateTemplate hibernateTemplate;

    public List<Customer> loadAll() {
        return hibernateTemplate.findAll(Customer.class);
    }

}
