package pl.wut.wsd.dsm.agent.customer_handler;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_handler.infrastructure.hibernate.HibernateTemplate;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.HibernateCustomerObligationRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

import java.util.Random;

@Slf4j
public class CustomerHandlerApplication {

    private static final String dbUrl = "jdbc:mysql://localhost:3306/wsd_dsm?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String dbUser = "wsd_dsm_user";
    private static final String dbPass = "user_!234";
    private static final Long customerId = (long) new Random().nextInt(100000); //TODO, unmock
    private static final String mainContainerHost = "localhost";
    private static final int mainContainerPort = 1099;

    public static void main(final String[] args) throws StaleProxyException {
        final HibernateTemplate template = new HibernateTemplate(dbUrl, dbUser, dbPass);
        final CustomerObligationRepository repository = new HibernateCustomerObligationRepository(template);

        final CustomerHandlerDependencies customerHandlerDependencies = new CustomerHandlerDependencies(new Customer(customerId), repository, Codec.json());

        final AgentContainer customerAgentContainer = createAgentContainer();

        log.info("Starting customer-handler for customer {}", customerId);

        final AgentController agentController = customerAgentContainer.createNewAgent("customer-handler" + customerId,
                CustomerHandlerAgent.class.getCanonicalName(), new Object[]{customerHandlerDependencies});
        agentController.start();
    }

    private static AgentContainer createAgentContainer() {
        final Runtime runtime = Runtime.instance();
        final Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "wsd-dsm");
        profile.setParameter(Profile.CONTAINER_NAME, "customer-handler-container");
        profile.setParameter(Profile.MAIN_HOST, mainContainerHost);
        profile.setParameter(Profile.MAIN_PORT, Integer.toString(mainContainerPort));

        return runtime.createAgentContainer(profile);
    }
}
