package pl.wut.wsd.dsm.agent.customer_agent;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.Options;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.infrastructure.InetUtils;
import pl.wut.wsd.dsm.infrastructure.properties.config.AgentConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.CommandLineConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.FileConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.MissingConfigEntryException;

import java.nio.file.Paths;

@Slf4j
public class CustomerAgentApplication {

    private static final String configFileOption = "configFile";
    private static final String mainContainerOption = "container";
    private static final String mainContainerPortOption = "port";
    private static final String customerIdOption = "cid";

    public static void main(final String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(configFileOption, true, "Path to configuration file");
        options.addOption(mainContainerOption, true, "Main container path");
        options.addOption(mainContainerPortOption, true, "Main container port");
        options.addOption(customerIdOption, true, "Customer id");

        final AgentConfiguration initialConfiguration = CommandLineConfiguration.of(options, args).throwingGet(Exception::new);
        final AgentConfiguration updatedConfiguration = initialConfiguration.getProperty(configFileOption, Paths::get)
                .map(FileConfiguration::fromPath)
                .map(r -> r.throwingGetRuntime(RuntimeException::new))
                .map(AgentConfiguration.class::cast)
                .orElse(initialConfiguration);

        final String hostname = updatedConfiguration.getProperty(mainContainerOption)
                .orElseThrow(() -> new MissingConfigEntryException(mainContainerOption, "container (string)"));

        final int containerPort = updatedConfiguration.getProperty(mainContainerPortOption, Integer::parseInt)
                .orElseThrow(() -> new MissingConfigEntryException(mainContainerPortOption, "port (integer)"));

        final Long customerID = updatedConfiguration.getProperty(customerIdOption, Long::parseLong)
                .orElseThrow(() -> new MissingConfigEntryException(customerIdOption, "customer id (long)"));


        final AgentContainer customerAgentContainer = createAgentContainer(hostname, containerPort, customerID);

        final int javalinPort = InetUtils.getFreePort();
        final Javalin javalin = startJavalin(javalinPort);

        final CustomerAgentDependencies dependencies = CustomerAgentDependencies.builder()
                .customer(new Customer(customerID))
                .javalin(javalin)
                .javalinPort(javalinPort)
                .build();

        log.info("Starting customer agent for customer {}", dependencies.getCustomer().getCustomerId());

        final AgentController agentController = customerAgentContainer.createNewAgent("customer-agent" + customerID, CustomerAgent.class.getCanonicalName(), new Object[]{dependencies});
        agentController.start();

    }

    private static Javalin startJavalin(final int freePort) {
        final JavalinConfig config = new JavalinConfig();
        config.enableCorsForAllOrigins();

        log.info("Starting rest api on port: " + freePort);

        return Javalin.create().start(freePort);
    }

    private static AgentContainer createAgentContainer(final String mainContainerHost, final int port, final Long customerId) {
        final Runtime runtime = Runtime.instance();
        final Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "wsd-dsm");
        profile.setParameter(Profile.CONTAINER_NAME, "customer-container" + customerId);
        profile.setParameter(Profile.MAIN_HOST, mainContainerHost);
        profile.setParameter(Profile.MAIN_PORT, Integer.toString(port));

        return runtime.createAgentContainer(profile);
    }

}
