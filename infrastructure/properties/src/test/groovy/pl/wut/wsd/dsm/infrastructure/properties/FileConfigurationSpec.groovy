package pl.wut.wsd.dsm.infrastructure.properties

import pl.wut.wsd.dsm.infrastructure.common.function.Result
import pl.wut.wsd.dsm.infrastructure.properties.config.AgentConfiguration
import pl.wut.wsd.dsm.infrastructure.properties.config.FileConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Path
import java.util.function.Function

class FileConfigurationSpec extends Specification {

    @Unroll
    def 'Should load properties from file specified by path #path'() {
        given:
        final Path path = new File(
                FileConfigurationSpec.class.getClassLoader().getResource('agent.properties').toURI()
        ).toPath()

        when:
        final Result<AgentConfiguration, Exception> configurationResult = FileConfiguration.fromPath(path)

        then:
        configurationResult.isValid()
        final configuration = configurationResult.result()
        configuration.getProperty('prop1', { Long.parseLong(it) }) == Optional.of(123L)
        configuration.getProperty('prop2', { Double.parseDouble(it) }) == Optional.of(20.123D)
        configuration.getProperty('prop3', Function.identity()) == Optional.of('xd')
        configuration.getProperty('noProp', Function.identity()) == Optional.empty()
    }

}
