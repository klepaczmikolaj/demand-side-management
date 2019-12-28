package pl.wut.wsd.dsm.agent.customer_handler;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.cli.Options;
import pl.wut.wsd.dsm.infrastructure.properties.config.AgentConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.CommandLineConfiguration;
import pl.wut.wsd.dsm.infrastructure.properties.config.FileConfiguration;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupInfo;

import java.nio.file.Paths;
import java.util.function.Function;

@Accessors(fluent = true)
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CustomerHandlerApplicationProperties implements AgentStartupInfo {

    private static final String dbUrlConfigKey = "dbUrl";
    private static final String dbUserConfigKey = "dbUser";
    private static final String dbPassConfigKey = "dbPass";
    private static final String customerIdConfigKey = "cid";
    private static final String containerHostConfigKey = "host";
    private static final String containerPortConfigKey = "port";
    private static final String configFileKey = "configFile";

    private final String dbUrl;
    private final String dbUser;
    private final String dbPass;
    private final Long customerId;
    private final String mainContainerHost;
    private final int mainContainerPort;

    static CustomerHandlerApplicationProperties parse(final String[] commandLineArgs) throws Exception {
        final Options options = prepareOptions();
        final AgentConfiguration initialConfiguration = CommandLineConfiguration.of(options, commandLineArgs).throwingGet(Function.identity());

        final AgentConfiguration configuration = initialConfiguration.getProperty(configFileKey)
                .map(Paths::get)
                .map(FileConfiguration::fromPath)
                .map(r -> r.throwingGetRuntime(RuntimeException::new))
                .map(fileConfig -> fileConfig.merge(initialConfiguration, AgentConfiguration.AgentConfigurationPriority.THIS))
                .orElse(initialConfiguration);

        return CustomerHandlerApplicationProperties.builder()
                .dbUrl(configuration.getThrowing(dbUrlConfigKey))
                .dbUser(configuration.getThrowing(dbUserConfigKey))
                .dbPass(configuration.getThrowing(dbPassConfigKey))
                .customerId(configuration.getThrowing(customerIdConfigKey, Long::parseLong))
                .mainContainerHost(configuration.getThrowing(containerHostConfigKey))
                .mainContainerPort(configuration.getThrowing(containerPortConfigKey, Integer::parseInt))
                .build();
    }


    private static Options prepareOptions() {
        final Options options = new Options();
        options.addOption(configFileKey, true, "Configuration file path")
                .addOption(dbUrlConfigKey, true, "Database connection url")
                .addOption(dbUserConfigKey, true, "DatabaseLogin")
                .addOption(dbPassConfigKey, true, "Database password")
                .addOption(customerIdConfigKey, true, "Customer id")
                .addOption(containerHostConfigKey, true, "Main container hostname")
                .addOption(containerPortConfigKey, true, "Main container port");

        return options;
    }

    @Override
    public String platformId() {
        return "wsd-dsm";
    }

    @Override
    public String containerName() {
        return "customer-handler-container";
    }

}
