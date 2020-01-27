package pl.wut.wsd.dsm.agent.customer_agent;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import jade.wrapper.AgentContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.Options;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.external.google.GoogleNotificationsAdapter;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.properties.config.AgentConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.CommandLineConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.FileConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.MissingConfigEntryException;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupInfoImpl;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupManager;

import java.nio.file.Paths;

@Slf4j
public class CustomerAgentApplication {

    private static final String configFileOption = "configFile";
    private static final String mainContainerOption = "container";
    private static final String mainContainerPortOption = "port";
    private static final String customerIdOption = "cid";
    private static final String firebaseToken = "firebaseToken";
    private static final String notificationsTokenKey = "notificationApiToken";

    private static final AgentStartupManager startupManager = new AgentStartupManager();
    private static final String notificationCustomerIdKey = "notificationId";

    public static void main(final String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(configFileOption, true, "Path to configuration file");
        options.addOption(mainContainerOption, true, "Main container path");
        options.addOption(mainContainerPortOption, true, "Main container port");
        options.addOption(customerIdOption, true, "Customer id");
        options.addOption(firebaseToken, true, "Firebase token");
        options.addOption(notificationsTokenKey, true, "Google notifications constant");
        options.addOption(notificationCustomerIdKey, true, "Customer google notifications id");

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

        final String notificationKey = updatedConfiguration.getProperty(notificationsTokenKey)
                .orElseThrow(() -> new MissingConfigEntryException(notificationsTokenKey, "notification token (String)"));

        final String customerNotificationId = updatedConfiguration.getProperty(notificationCustomerIdKey)
                .orElseThrow(() -> new MissingConfigEntryException(notificationCustomerIdKey, "Notification customer id (String)"));


        final AgentContainer container = createAgentContainer(hostname, containerPort, customerID);

        final Javalin javalin = startJavalin(customerID.intValue());

        final CustomerAgentDependencies dependencies = CustomerAgentDependencies.builder()
                .customer(new Customer(customerID))
                .javalin(javalin)
                .javalinPort(customerID.intValue())
                .codec(Codec.json())
                .notificationAdapter(new GoogleNotificationsAdapter(notificationKey, customerNotificationId))
                .build();

        startupManager.startAgent(container, CustomerAgent.class, "customer-agent" + customerID, dependencies);
    }

    private static Javalin startJavalin(final int freePort) {
        final JavalinConfig config = new JavalinConfig();
        config.enableCorsForAllOrigins();

        log.info("Starting rest api on port: " + freePort);

        return Javalin.create().start(freePort);
    }

    private static AgentContainer createAgentContainer(final String mainContainerHost, final int port, final Long customerId) {
        return startupManager.startChildContainer(AgentStartupInfoImpl.builder()
                .platformId("wsd-dsm")
                .containerName("customer-container" + customerId)
                .mainContainerHost(mainContainerHost)
                .mainContainerPort(port)
                .build());
    }

}
