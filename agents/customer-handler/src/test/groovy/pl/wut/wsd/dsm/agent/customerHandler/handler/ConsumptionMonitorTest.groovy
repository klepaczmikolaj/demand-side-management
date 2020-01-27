package pl.wut.wsd.dsm.agent.customerHandler.handler

import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerObligationRepository
import pl.wut.wsd.dsm.infrastructure.codec.Codec
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumption
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumptionProtocol
import spock.lang.Specification

import java.time.ZonedDateTime

class ConsumptionMonitorTest extends Specification {

//    /* Fixed date for testing purposes */
    private static final ZonedDateTime ZD = ZonedDateTime.now()

    private static final List<Obligation> MOCK_OBLIGATIONS = [
            new Obligation(id: 1L, relatedOffer: new Offer(demandChangeSince: ZD, demandChangeUntil: ZD.plusDays(5))),
            new Obligation(id: 2L, relatedOffer: new Offer(demandChangeSince: ZD.minusDays(5), demandChangeUntil: ZD.minusDays(2))),
            new Obligation(id: 3L, relatedOffer: new Offer(demandChangeSince: ZD.minusDays(10), demandChangeUntil: ZD.plusDays(10))),
    ]

    final Codec codec = Mock()
    final Long customerId = 5L
    final EnergyConsumptionProtocol.ConsumptionInformation step = EnergyConsumptionProtocol.INSTANCE.informationStep(customerId)
    final CustomerObligationRepository repository = Mock() {
        getAllUnresolved(customerId) >> MOCK_OBLIGATIONS
    }

    private final ConsumptionMonitor monitor = new ConsumptionMonitor(codec, step, repository, customerRepository, customerId)


    def "Should add new consumption entry only to ongoing obligations"() {
        given:
        final EnergyConsumption consumption = new EnergyConsumption(ZD.plusHours(5), 20)

        when:
        monitor.handle(consumption)

        then:
        true
        monitor.consumptionEntries == [1L: consumption, 3L: consumption]
    }

}
