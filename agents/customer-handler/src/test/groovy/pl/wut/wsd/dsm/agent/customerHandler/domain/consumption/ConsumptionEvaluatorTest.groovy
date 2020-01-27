package pl.wut.wsd.dsm.agent.customerHandler.domain.consumption

import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumption
import spock.lang.Specification

import java.time.ZonedDateTime

class ConsumptionEvaluatorTest extends Specification {

    private static final ZonedDateTime d = ZonedDateTime.now()
    private final ConsumptionEvaluator consumptionEvaluator = new ConsumptionEvaluator()

    def "Should evaluate both average consumption and evaluate total and percentage of time and choose worse"() {
        given:
        final Customer customer = new Customer(customerId: 123L, nominalUsageInWatts: 800)
        final Offer relatedOffer = new Offer(offerId: UUID.randomUUID(), demandChangeSince: d, demandChangeUntil: d.plusHours(2), type: Offer.Type.REDUCTION)
        final Obligation obligation = Obligation.newObligation(customer, 0.5, relatedOffer)

        when:
        final kept = consumptionEvaluator.evaluateToPercentageKept(customer, consumption, obligation)

        then:
        kept == expectedPercentage

        where:
        consumption                     | expectedPercentage
        [new EnergyConsumption(d, 600),
         new EnergyConsumption(d, 500),
         new EnergyConsumption(d, 300),
         new EnergyConsumption(d, 200),
         new EnergyConsumption(d, 600)] | 40
    }
}
