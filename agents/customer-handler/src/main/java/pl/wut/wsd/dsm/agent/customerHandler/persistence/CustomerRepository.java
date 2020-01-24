package pl.wut.wsd.dsm.agent.customerHandler.persistence;

import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findByCustomerId(final Long customerId);

    void save(Customer customer);
}

