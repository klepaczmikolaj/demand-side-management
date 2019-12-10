package pl.wut.wsd.dsm.agent.network_advisor.domain

import pl.wut.wsd.dsm.agent.network_advisor.weather.model.BasicWeatherIndicators
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecastEntry
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.Wind
import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZonedDateTime

class DefaultElectricityProductionProfileTest extends Specification {

    final ElectricityProductionProfileCalculator calculator = new DefaultElectricityProductionProfileCalculator()

    @Unroll
    def 'Should provide nice result profile'() {
        when:
        final profile = calculator.calculate(forecast)

        then:
        expectedProduction.forEach { timestamp, expecetedWatts ->
            assert profile.getProductionInWatts(timestamp) == expecetedWatts
        }

        where:
        forecast                                       | expectedProduction
        new WeatherForecast()                          | [(ZonedDateTime.now()): BigInteger.valueOf(50L)]
        new WeatherForecast(entries:
                [new WeatherForecastEntry(base: new BasicWeatherIndicators(temperature: -10.0),
                        wind: new Wind(speed: 10.0))]) | [(ZonedDateTime.now()): BigInteger.valueOf(100L)]
    }

    int multiply(int i1, int i2) {
        return i1 * i2
    }

    @Unroll
    def 'Should properly multiply #number1 and #number2 and produce #expectedResult'() {
        expect:
        multiply(number1, number2) == expectedResult

        where:
        number1 | number2 | expectedResult
        0       | 0       | 0
        0       | 1       | 0
        0       | 2       | 0
        0       | 3       | 0
        0       | 4       | 0
        0       | 5       | 0
        0       | 6       | 0
        0       | 7       | 0
        0       | 8       | 0
        0       | 9       | 0
        1       | 0       | 0
        1       | 1       | 1
        1       | 2       | 2
        1       | 3       | 3
        1       | 4       | 4
        1       | 5       | 5
        1       | 6       | 6
        1       | 7       | 7
        1       | 8       | 8
        1       | 9       | 9


    }
}
