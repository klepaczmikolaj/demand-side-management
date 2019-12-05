package pl.wut.wsd.dsm.agent.customer_agent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;

import java.util.Random;

@Slf4j
public class CustomerAgentApplication {

    private static final String mainContainerHost = "localhost";
    private static final int mainContainerPort = 1099;

    public static void main(final String[] args) throws StaleProxyException {
        final AgentContainer customerAgentContainer = createAgentContainer();

        for (int i = 0; i < 5; i++) {
            final long customerId = (long) new Random().nextInt(100000);
            final Customer customer = new Customer(customerId);
            final CustomerAgentDependencies dependencies = new CustomerAgentDependencies(customer);
            log.info("Starting customer-agent for customer {}", customerId);
            final AgentController agentController = customerAgentContainer.createNewAgent("customer-agent" + customerId, CustomerAgent.class.getCanonicalName(), new Object[]{dependencies});
            agentController.start();
        }

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
