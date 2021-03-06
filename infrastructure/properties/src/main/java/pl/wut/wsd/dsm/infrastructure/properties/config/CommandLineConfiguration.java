package pl.wut.wsd.dsm.infrastructure.properties.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandLineConfiguration extends AgentConfiguration {

    private final CommandLine commandLine;
    private final Options options;


    public static Result<CommandLineConfiguration, Exception> of(final Options options, final String[] args) {
        final CommandLineParser parser = new DefaultParser();
        try {
            final CommandLine parsed = parser.parse(options, args);

            return Result.ok(new CommandLineConfiguration(parsed, options));
        } catch (final ParseException e) {
            return Result.error(e);
        }
    }

    @Override
    public Optional<String> getProperty(final String key) {
        final String optionValue = commandLine.getOptionValue(key);

        return Optional.ofNullable(optionValue);
    }

    @Override
    protected Map<String, String> toPropertiesMap() {
        return Arrays.stream(commandLine.getOptions()).collect(Collectors.toMap(Option::getOpt, Option::getValue));
    }

    public void printUsageInformation(final String applicationName) {
        new HelpFormatter().printHelp(applicationName, "Usage", options, "All rights reserved.");
    }
}
