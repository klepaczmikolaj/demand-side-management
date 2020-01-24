package pl.wut.wsd.dsm.agent.customerHandler.persistence;

import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerObligationRepository {

    List<Obligation> getCustomerObligations(Long customerId);

    List<Obligation> getAllUnresolved(Long customerId);

    Optional<Obligation> getById(final Long id);

    Optional<Obligation> findByRelatedOfferId(final UUID relatedOfferId);

    boolean isResolved(Long obligationId);

    void saveOrUpdate(Obligation obligation);
}
