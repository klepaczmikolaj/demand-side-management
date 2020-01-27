package pl.wut.wsd.dsm.agent.customer_agent.persistence.repo;

import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findByCustomerId(final Long customerId);

    void save(Customer customer);
}

