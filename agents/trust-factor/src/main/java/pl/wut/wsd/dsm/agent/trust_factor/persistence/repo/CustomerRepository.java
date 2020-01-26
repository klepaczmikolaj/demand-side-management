package pl.wut.wsd.dsm.agent.trust_factor.persistence.repo;

import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findByCustomerId(final Long customerId);

    void save(Customer customer);

    List<Customer> findAllCustomers();
}

