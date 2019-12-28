package pl.wut.wsd.dsm.agent.trust_factor.ranking

import pl.wut.wsd.dsm.agent.trust_factor.TestHibernateTemplate
import pl.wut.wsd.dsm.agent.trust_factor.persistence.RankingReader
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest
import spock.lang.Specification

class RankingReaderTest extends Specification {

    private final HibernateTemplate hibernateTemplate = new TestHibernateTemplate()
    private final RankingReader rankingReader = new RankingReader(hibernateTemplate)

    def 'Should find sorted customers'() {
        given:
        (1..10).each { persistCustomerTrust(it, it) }

        when:
        final result = rankingReader.getRanking(request)

        then:
        result.rankingEntries.collect { it.customer.customerId.intValue() } == expectedResultIds

        where:
        request                                     | expectedResultIds
        new GetTrustRankingRequest(from: 0, to: 10) | [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
        new GetTrustRankingRequest(from: 9, to: 10) | [10]
    }

    def 'Should select obligations properly using obligations view - this check test view'() {
        when:
        def all = hibernateTemplate.findAll({ final r, final cb -> cb.and() }, Obligation.class)

        then:
        noExceptionThrown()
        !all.isEmpty()
    }

    def 'cleanup'() {
        hibernateTemplate.deleteAll(CustomerTrust.class)
    }

    private void persistCustomerTrust(final long customerId, final double value) {
        hibernateTemplate.saveOrUpdate(new CustomerTrust(customerId: customerId, currentValue: value, kwsProcessed: 100.0))
    }
}
