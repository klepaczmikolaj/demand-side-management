package pl.wut.wsd.dsm.agent.customer_agent.persistence.repo;

import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Obligation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerObligationRepository {

    List<Obligation> getCustomerObligations(Long customerId);

    List<Obligation> getAllUnresolved(Long customerId);

    Optional<Obligation> getById(final Long id);

    Optional<Obligation> findByRelatedOfferId(final UUID relatedOfferId);

    boolean isResolved(Long obligationId);
}
