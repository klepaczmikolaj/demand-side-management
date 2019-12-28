package pl.wut.wsd.dsm.infrastructure.properties

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import pl.wut.wsd.dsm.infrastructure.properties.config.CommandLineConfiguration
import spock.lang.Specification

import java.util.function.Function

class CommandLineConfigurationTest extends Specification {


    def 'Should properly parse command line args #commandLine'() {
        given:
        final Options options = new Options()
        options.addOption('v', true, 'prints verbose output')
        options.addOption('x', true, 'prints errors')
        options.addRequiredOption('id', 'customerId', true, 'related agent customer id')
        new HelpFormatter().printHelp('test', options)

        when:
        final configResult = CommandLineConfiguration.of(options, commandLine.split('\\s'))

        then:
        configResult.isValid()
        final config = configResult.result()
        expectations.forEach { final k, final v -> assert config.getProperty(k, Function.identity()).get() == v }
        !config.getProperty('v', Function.identity()).isPresent()

        where:
        commandLine    | expectations
        '-x d -id 123' | ['x': 'd', 'customerId': '123']
    }

    def 'Should properly parse options to option map'() {
        given:
        final Options options = new Options()
        options.addOption('v', true, 'prints verbose output')
        options.addOption('x', true, 'prints errors')
        options.addRequiredOption('id', 'customerId', true, 'related agent customer id')
        new HelpFormatter().printHelp('test', options)

        when:
        final configResult = CommandLineConfiguration.of(options, commandLine.split('\\s'))

        then:
        configResult.isValid()
        configResult.result().toPropertiesMap() == expectations

        where:
        commandLine    | expectations
        '-x d -id 123' | ['x': 'd', 'id': '123']
    }
}
