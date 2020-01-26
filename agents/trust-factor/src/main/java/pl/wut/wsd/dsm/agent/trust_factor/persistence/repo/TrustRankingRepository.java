package pl.wut.wsd.dsm.agent.trust_factor.persistence.repo;

import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust;

import java.util.Optional;

public interface TrustRankingRepository {

    Optional<CustomerTrust> getForCustomer(final Customer customer);

    Optional<CustomerTrust> findByCustomerId(final Long customerId);

    void save(final CustomerTrust customerTrust);

}
