package pl.wut.wsd.dsm.agent.network_advisor.domain

import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.InterpolatedElectricityDemandProfile
import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZonedDateTime

class InterpolatedElectricityDemandProfileTest extends Specification {

    private static final ZonedDateTime fixedTime = ZonedDateTime.now()

    @Unroll
    def 'Should provide nice result profile'() {
        given:
        final InterpolatedElectricityDemandProfile profile = new InterpolatedElectricityDemandProfile(productionPoints)

        when:
        profile.getDemandInWatts(fixedTime.plusHours(1))

        then:
        new BigInteger(expectedResult) == new BigInteger(expectedResult)

        where:
        productionPoints                                                                                | expectedResult
        [(fixedTime): new BigInteger(0), (fixedTime.plusHours(2)): new BigInteger(100)]                 | 50
        [(fixedTime): new BigInteger(100), (fixedTime.minusHours(1)): new BigInteger(50)]               | 150
        [(fixedTime)              : new BigInteger(100), (fixedTime.minusHours(1)): new BigInteger(50),
         (fixedTime.minusHours(3)): new BigInteger(200), (fixedTime.plusHours(5)): new BigInteger(500)] | 150
    }
}