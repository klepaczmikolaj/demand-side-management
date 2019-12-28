package pl.wut.wsd.dsm.agent.trust_factor.ranking


import pl.wut.wsd.dsm.agent.trust_factor.TestHibernateTemplate
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerObligationState
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrustRefreshDetails
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust_
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.time.ZonedDateTime

class TrustRankingRefresherTest extends Specification {

    @Shared
    private static HibernateTemplate hibernateTemplate = new TestHibernateTemplate()

    @Subject
    private final TrustRankingRefresher refresher = new TrustRankingRefresher(hibernateTemplate)

    def setupSpec() {
        final details = hibernateTemplate.findOne({ final r, final cb -> cb.and() }, CustomerTrustRefreshDetails.class)
                .orElseGet({ new CustomerTrustRefreshDetails(lastRefreshed: ZonedDateTime.now().minusDays(15)) })
        hibernateTemplate.saveOrUpdate(details)
    }

    def cleanup() {
        hibernateTemplate.deleteAll(Obligation.class)
    }

    def 'Should recompute ranking'() {
        given:
        final long customerId = 234
        final List<Obligation> obligations = [
                new Obligation(sizeKws: 100, perecentageKept: 50, state: CustomerObligationState.FAILED, customerId: customerId,
                        since: ZonedDateTime.now().minusDays(10), until: ZonedDateTime.now().minusDays(1)),
                new Obligation(sizeKws: 100, perecentageKept: 100, state: CustomerObligationState.KEPT, customerId: customerId,
                        since: ZonedDateTime.now().minusDays(10), until: ZonedDateTime.now().minusDays(1))
        ]
        obligations.forEach { hibernateTemplate.saveOrUpdate(it) }

        when:
        refresher.refresh()

        then:
        hibernateTemplate.findOne({ final r, final cb -> cb.equal(r.get(CustomerTrust_.customerId), customerId) }, CustomerTrust.class)
        hibernateTemplate.findOne({ final r, final cb -> cb.and() }, CustomerTrustRefreshDetails.class).get().lastRefreshed.isAfter(ZonedDateTime.now().minusMinutes(1))
    }
}
