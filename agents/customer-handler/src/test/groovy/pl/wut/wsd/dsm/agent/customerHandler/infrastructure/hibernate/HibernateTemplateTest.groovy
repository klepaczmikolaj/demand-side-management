package pl.wut.wsd.dsm.agent.customerHandler.infrastructure.hibernate

import pl.wut.wsd.dsm.agent.customerHandler.domain.model.CustomerObligationState
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer
import pl.wut.wsd.dsm.agent.customerHandler.test.features.TestHibernateTemplate
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate
import spock.lang.Specification

import java.time.ZonedDateTime

class HibernateTemplateTest extends Specification {

    private final HibernateTemplate template = new TestHibernateTemplate()

    def setup() {
        template.deleteAll(Obligation.class)
    }

    def 'Should save object and fetch it by id'() {
        given:
        final Offer offer = new Offer(offerId: UUID.randomUUID(), customerId: 123l, validUntil: ZonedDateTime.now(),
                state: Offer.State.PENDING, type: Offer.Type.INCREASE, kws: 1237, pricePerKw: BigDecimal.ONE, demandChangeSince: ZonedDateTime.now(), demandChangeUntil: ZonedDateTime.now()
        )

        final Obligation obligation = new Obligation(
                customerId: new Random().nextLong(),
                state: CustomerObligationState.DURING_EVALUATION,
                sizeKws: 100.50,
                perecentageKept: 21.35
        )

        when:
        template.saveOrUpdate(offer)
        obligation.relatedOffer = offer
        template.saveOrUpdate(obligation)

        then:
        obligation.id != null

        when:
        final reloaded = template.getOne(obligation.id, obligation.class)

        then:
        reloaded.isPresent()
    }


}
