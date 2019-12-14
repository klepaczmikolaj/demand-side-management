package pl.wut.wsd.dsm.agent.customer_handler;

import com.mysql.jdbc.Driver;
import jade.wrapper.AgentContainer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.MySQL8Dialect;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_handler.model.Obligation;
import pl.wut.wsd.dsm.agent.customer_handler.model.Offer;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.HibernateCustomerObligationRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CustomerHandlerApplication {

    private static final AgentStartupManager agentStartupManager = new AgentStartupManager();
    private final static Set<Class<?>> jpaClasses = new HashSet<>(Arrays.asList(Obligation.class, Offer.class));

    public static void main(final String[] args) throws Exception {
        final CustomerHandlerApplicationProperties properties = CustomerHandlerApplicationProperties.parse(args);

        final HibernateTemplate template = new HibernateTemplate(properties.dbUrl(), properties.dbUser(), properties.dbPass(), Driver.class, MySQL8Dialect.class, jpaClasses);
        final CustomerObligationRepository repository = new HibernateCustomerObligationRepository(template);

        final CustomerHandlerDependencies dependencies = new CustomerHandlerDependencies(new Customer(properties.customerId()), repository, Codec.json());

        final AgentContainer customerAgentContainer = agentStartupManager.startChildContainer(properties);

        log.info("Starting customer-handler for customer {}", properties.customerId());

        agentStartupManager.startAgent(customerAgentContainer, CustomerHandlerAgent.class, "customer-handler" + properties.customerId(), dependencies);
    }

}
