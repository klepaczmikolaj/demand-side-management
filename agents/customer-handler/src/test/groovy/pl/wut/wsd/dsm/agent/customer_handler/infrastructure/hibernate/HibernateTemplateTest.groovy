package pl.wut.wsd.dsm.agent.customer_handler.infrastructure.hibernate

import pl.wut.wsd.dsm.agent.customer_handler.model.CustomerObligationState
import pl.wut.wsd.dsm.agent.customer_handler.model.Obligation
import pl.wut.wsd.dsm.agent.customer_handler.model.Obligation_
import spock.lang.Specification

import java.time.ZonedDateTime

class HibernateTemplateTest extends Specification {

    private final static String url = 'jdbc:mysql://localhost:3306/wsd_dsm?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC'
    private static final String user = 'wsd_dsm_user'
    private static final String password = 'user_!234'

    private final HibernateTemplate template = new HibernateTemplate(url, user, password)

    def setup() {
        template.deleteAll(Obligation.class)
    }

    def 'Should save object and fetch it by id'() {
        given:
        final Obligation obligation = new Obligation(
                customerId: new Random().nextLong(),
                state: CustomerObligationState.DURING_EVALUATION,
                sizeKws: 100.50,
                perecentageKept: 21.35,
                since: ZonedDateTime.now(),
                until: ZonedDateTime.now().plusDays(2)
        )

        when:
        template.saveOrUpdate(obligation)

        then:
        obligation.id != null

        when:
        final reloaded = template.getOne(obligation.id, obligation.class)

        then:
        reloaded.isPresent()
    }

    def 'Should update object'() {
        given:
        final Obligation obligation = new Obligation(
                customerId: new Random().nextLong(),
                state: CustomerObligationState.DURING_EVALUATION,
                sizeKws: 100.50,
                perecentageKept: 21.35,
                since: ZonedDateTime.now(),
                until: ZonedDateTime.now().plusDays(2)
        )

        when:
        template.saveOrUpdate(obligation)

        then:
        obligation.id != null

        when:
        obligation.setPerecentageKept(25)
        obligation.setSizeKws(150)
        template.saveOrUpdate(obligation)

        then:
        def reloaded = template.getOne(obligation.id, obligation.class)
        reloaded.isPresent()
        reloaded.get().perecentageKept == 25
        reloaded.get().sizeKws == 150
    }

    def 'Should filter by specification'() {
        given:
        saveObligationForCustomer(1)
        saveObligationForCustomer(2)
        saveObligationForCustomer(3)

        expect:
        template.findOne({ final r, final cb -> cb.equal(r.get(Obligation_.customerId), 1L) }, Obligation.class).isPresent()
        and:
        !template.findOne({ final r, final cb -> cb.equal(r.get(Obligation_.customerId), '1234') }, Obligation.class).isPresent()
    }

    private Obligation saveObligationForCustomer(final Long customerId) {
        final Obligation obligation = new Obligation(
                customerId: customerId,
                state: CustomerObligationState.DURING_EVALUATION,
                sizeKws: 100.50,
                perecentageKept: 21.35,
                since: ZonedDateTime.now(),
                until: ZonedDateTime.now().plusDays(2)
        )
        template.saveOrUpdate(obligation)
        return obligation
    }
}
