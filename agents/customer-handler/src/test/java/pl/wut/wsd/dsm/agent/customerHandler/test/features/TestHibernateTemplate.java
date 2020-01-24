package pl.wut.wsd.dsm.agent.customerHandler.test.features;

import com.mysql.jdbc.Driver;
import org.hibernate.dialect.MySQL8Dialect;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;

import java.util.Arrays;
import java.util.HashSet;

public class TestHibernateTemplate extends HibernateTemplate {

    private final static String url = "jdbc:mysql://localhost:3306/wsd_dsm?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "wsd_dsm_user";
    private static final String password = "user_!234";

    public TestHibernateTemplate() {
        super(url, user, password, Driver.class, MySQL8Dialect.class, new HashSet<>(Arrays.asList(Obligation.class, Offer.class)));
    }

}
