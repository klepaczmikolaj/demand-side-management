package pl.wut.wsd.dsm.agent.customer_agent;

import com.mysql.jdbc.Driver;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import jade.wrapper.AgentContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.Options;
import org.hibernate.dialect.MySQL8Dialect;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Obligation;
import pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Offer;
import pl.wut.wsd.dsm.agent.external.google.GoogleNotificationsAdapter;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;
import pl.wut.wsd.dsm.infrastructure.properties.config.AgentConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.CommandLineConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.FileConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.MissingConfigEntryException;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupInfoImpl;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupManager;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

@Slf4j
public class CustomerAgentApplication {

    private static final String configFileOption = "configFile";
    private static final String mainContainerOption = "container";
    private static final String mainContainerPortOption = "port";
    private static final String customerIdOption = "cid";
    private static final String notificationsTokenKey = "notificationApiToken";
    private static final String dbUrlConfigKey = "dbUrl";
    private static final String dbUserConfigKey = "dbUser";
    private static final String dbPassConfigKey = "dbPass";

    private static final AgentStartupManager startupManager = new AgentStartupManager();

    public static void main(final String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(configFileOption, true, "Path to configuration file")
                .addOption(mainContainerOption, true, "Main container path")
                .addOption(mainContainerPortOption, true, "Main container port")
                .addOption(customerIdOption, true, "Customer id")
                .addOption(notificationsTokenKey, true, "Google notifications constant")
                .addOption(dbUrlConfigKey, true, "Database connection url")
                .addOption(dbUserConfigKey, true, "DatabaseLogin")
                .addOption(dbPassConfigKey, true, "Database password");

        final AgentConfiguration initialConfiguration = CommandLineConfiguration.of(options, args).throwingGet(Exception::new);
        final AgentConfiguration updatedConfiguration = initialConfiguration.getProperty(configFileOption, Paths::get)
                .map(FileConfiguration::fromPath)
                .map(r -> r.throwingGetRuntime(RuntimeException::new))
                .map(o -> o.merge(initialConfiguration, AgentConfiguration.AgentConfigurationPriority.OTHER))
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

        final String dbUrl = updatedConfiguration.getProperty(dbUrlConfigKey)
                .orElseThrow(() -> new MissingConfigEntryException(dbUrlConfigKey, "Db url (url)"));


        final String dbLogin = updatedConfiguration.getProperty(dbUserConfigKey)
                .orElseThrow(() -> new MissingConfigEntryException(dbUserConfigKey, "Database user name(String)"));


        final String dbPass = updatedConfiguration.getProperty(dbPassConfigKey)
                .orElseThrow(() -> new MissingConfigEntryException(dbPassConfigKey, "Database user password(String)"));

        final AgentContainer container = createAgentContainer(hostname, containerPort, customerID);

        final Javalin javalin = startJavalin(customerID.intValue());

        final HibernateTemplate template = new HibernateTemplate(dbUrl, dbLogin, dbPass, Driver.class, MySQL8Dialect.class,
                new HashSet<>(Arrays.asList(pl.wut.wsd.dsm.agent.customer_agent.persistence.model.Customer.class, Offer.class, Obligation.class)));

        final CustomerAgentDependencies dependencies = CustomerAgentDependencies.builder()
                .customer(new Customer(customerID))
                .javalin(javalin)
                .javalinPort(customerID.intValue())
                .codec(Codec.json())
                .notificationAdapter(new GoogleNotificationsAdapter(notificationKey))
                .hibernateTemplate(template)
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
