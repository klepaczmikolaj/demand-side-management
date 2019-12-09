package pl.wut.wsd.dsm.agent.trust_factor.ranking

import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerObligationState
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation
import spock.lang.Specification
import spock.lang.Subject

class TrustRankingComputerTest extends Specification {

    @Subject
    private final TrustRankingComputer trustRankingComputer = new TrustRankingComputer()

    def 'Should recompute trust ranking'() {
        given:
        final double currentValue = 5.0
        final double kwsProcessed = 50.0

        final CustomerTrust customerTrust = new CustomerTrust(currentValue: currentValue, kwsProcessed: kwsProcessed)

        when:
        final TrustRankingComputer.TrustComputationResult compute = trustRankingComputer.compute(customerTrust, obligations as Set)

        then:
        compute == expected

        where:
        obligations                                                                                | expected
        [new Obligation(sizeKws: 50, state: CustomerObligationState.FAILED, perecentageKept: 0.0)] | new TrustRankingComputer.TrustComputationResult(2.5D, 100.0D)


    }
}
