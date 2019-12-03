package pl.wut.wsd.dsm.agent.customer_handler.persistence;

import pl.wut.wsd.dsm.agent.customer_handler.model.Obligation;

import java.util.List;
import java.util.UUID;

public interface CustomerObligationRepository {

    List<Obligation> getCustomerObligations(UUID customerId);
    void saveOrUpdate(Obligation obligation);
}
