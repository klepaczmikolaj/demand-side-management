package pl.wut.wsd.dsm.agent.trust_factor;

import com.mysql.jdbc.Driver;
import jade.wrapper.AgentContainer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.MySQL8Dialect;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.RankingReader;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrustRefreshDetails;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.repo.DefaultTrustRankingRepository;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.repo.HibernateCustomerRepository;
import pl.wut.wsd.dsm.agent.trust_factor.ranking.TrustRankingRefresher;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupManager;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
public class CustomerTrustApplication {

    public static void main(final String[] args) throws Exception {
        final TrustAgentApplicationProperties properties = TrustAgentApplicationProperties.parse(args);
        final AgentStartupManager agentStartupManager = new AgentStartupManager();

        final CustomerTrustAgentDependencies dependencies = prepareDependencies(properties);

        final AgentContainer container = agentStartupManager.startChildContainer(properties);
        agentStartupManager.startAgent(container, CustomerTrustAgent.class, "trust-agent", dependencies);
    }

    private static CustomerTrustAgentDependencies prepareDependencies(final TrustAgentApplicationProperties properties) {
        final HibernateTemplate hibernateTemplate = new HibernateTemplate(
                properties.dbUrl(),
                properties.dbUser(),
                properties.dbPass(),
                Driver.class,
                MySQL8Dialect.class,
                new HashSet<>(Arrays.asList(CustomerTrust.class, Obligation.class, CustomerTrustRefreshDetails.class, Customer.class))
        );

        return CustomerTrustAgentDependencies.builder()
                .rankingReader(new RankingReader(hibernateTemplate))
                .trustRankingRefresher(new TrustRankingRefresher(hibernateTemplate))
                .customerRepository(new HibernateCustomerRepository(hibernateTemplate))
                .trustRankingRepository(new DefaultTrustRankingRepository(hibernateTemplate))
                .build();
    }
}
