package pl.wut.wsd.dsm.agent.customer_handler;

import jade.wrapper.AgentContainer;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_handler.infrastructure.hibernate.HibernateTemplate;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.HibernateCustomerObligationRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupManager;

@Slf4j
public class CustomerHandlerApplication {

    private static final AgentStartupManager agentStartupManager = new AgentStartupManager();

    public static void main(final String[] args) throws Exception {
        final CustomerHandlerApplicationProperties properties = CustomerHandlerApplicationProperties.parse(args);

        final HibernateTemplate template = new HibernateTemplate(properties.dbUrl(), properties.dbUser(), properties.dbPass());
        final CustomerObligationRepository repository = new HibernateCustomerObligationRepository(template);

        final CustomerHandlerDependencies dependencies = new CustomerHandlerDependencies(new Customer(properties.customerId()), repository, Codec.json());

        final AgentContainer customerAgentContainer = agentStartupManager.startChildContainer(properties);

        log.info("Starting customer-handler for customer {}", properties.customerId());

        agentStartupManager.startAgent(customerAgentContainer, CustomerHandlerAgent.class, "customer-handler" + properties.customerId(), dependencies);
    }

}
