package pl.wut.wsd.dsm.agent.trust_factor

import com.mysql.jdbc.Driver
import org.hibernate.dialect.MySQL8Dialect
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrustRefreshDetails
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate

class TestHibernateTemplate extends HibernateTemplate {

    TestHibernateTemplate() {
        super(
                'jdbc:mysql://localhost:3306/wsd_dsm?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC',
                'wsd_dsm_user', 'user_!234', Driver.class, MySQL8Dialect.class, [Obligation.class, CustomerTrust.class, CustomerTrustRefreshDetails.class] as Set
        )
    }
}
