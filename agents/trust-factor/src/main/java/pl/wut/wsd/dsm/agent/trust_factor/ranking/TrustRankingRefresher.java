package pl.wut.wsd.dsm.agent.trust_factor.ranking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrustRefreshDetails;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust_;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.specification.AllObligationsResolved;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TrustRankingRefresher {

    private final HibernateTemplate hibernateTemplate;
    private final TrustRankingComputer computer = new TrustRankingComputer();

    public void refresh() {
        final CustomerTrustRefreshDetails refreshDetails = hibernateTemplate.findOne(((root, cb) -> cb.and()), CustomerTrustRefreshDetails.class).get();
        final ZonedDateTime lastRefreshed = refreshDetails.getLastRefreshed();
        log.info("Refreshing trust ranking, last refresh was: {}", lastRefreshed);
        final AllObligationsResolved specification = AllObligationsResolved.after(lastRefreshed);

        //TODO, read and write in transaction
        final List<Obligation> obligations = hibernateTemplate.findAll(specification, Obligation.class);

        final Map<Long, Set<Obligation>> obligationsByCustomer = obligations.stream().collect(Collectors.groupingBy(Obligation::getCustomerId, Collectors.toSet()));

        obligationsByCustomer.forEach(this::updateTrustRankingForCustomer);

        refreshDetails.setLastRefreshed(ZonedDateTime.now());

        hibernateTemplate.saveOrUpdate(refreshDetails);
        //TODO End of transaction
        log.info("Customer trust ranking refreshed, affected customers: {}", obligationsByCustomer.keySet());
    }


    private void updateTrustRankingForCustomer(final Long customerId, final Set<Obligation> obligations) {
        final CustomerTrust customerTrust = hibernateTemplate.findOne((r, cb) -> cb.equal(r.get(CustomerTrust_.customerId), customerId), CustomerTrust.class)
                .orElseGet(() -> CustomerTrust.forCustomer(customerId));

        final TrustRankingComputer.TrustComputationResult computed = computer.compute(customerTrust, obligations);
        customerTrust.setCurrentValue(computed.newValue);
        customerTrust.setKwsProcessed(computed.processed);

        hibernateTemplate.saveOrUpdate(customerTrust);
    }
}
