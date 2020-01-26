package pl.wut.wsd.dsm.agent.trust_factor.ranking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrustRefreshDetails;
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

        final Map<Customer, Set<Obligation>> obligationsByCustomer = obligations.stream().collect(Collectors.groupingBy(Obligation::getCustomer, Collectors.toSet()));

        obligationsByCustomer.forEach(this::updateTrustRankingForCustomer);

        refreshDetails.setLastRefreshed(ZonedDateTime.now());

        hibernateTemplate.saveOrUpdate(refreshDetails);
        //TODO End of transaction
        log.info("Customer trust ranking refreshed, affected customers: {}", obligationsByCustomer.keySet());
    }


    private void updateTrustRankingForCustomer(final Customer customer, final Set<Obligation> obligations) {
        if (customer.getTrust() == null) {
            customer.setTrust(CustomerTrust.forCustomer(customer));
        }

        final TrustRankingComputer.TrustComputationResult computed = computer.compute(customer.getTrust(), obligations);
        customer.getTrust().setKwsProcessed(computed.processed);

        hibernateTemplate.saveOrUpdate(customer);
    }
}
