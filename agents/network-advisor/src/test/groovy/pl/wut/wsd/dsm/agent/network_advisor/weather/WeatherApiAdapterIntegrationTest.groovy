package pl.wut.wsd.dsm.agent.network_advisor.weather

import pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest.RestClient
import spock.lang.Specification
import spock.lang.Subject

//@Ignore("Requires internet connection")
class WeatherApiAdapterIntegrationTest extends Specification {

    @Subject
    private final WeatherApiAdapter weatherApiAdapter = new WeatherApiAdapter(RestClient.defaultClient(), "d250a33a932b1f3ea88676644df45ae3", "Warsaw,pl")

    def 'Should call weather api with no exceptions'() {
        when:
        weatherApiAdapter.getWeatherForecast()

        then:
        noExceptionThrown()
    }

    def 'Should properly parse response'() {
        when:
        final result = weatherApiAdapter.getWeatherForecast()

        then:
        result.code == '200'
        result.entries != null
        !result.entries.isEmpty()
        result.entries.every { it.base.temperature != null }
        result.entries.every { it.cloudiness != null }
    }
}
