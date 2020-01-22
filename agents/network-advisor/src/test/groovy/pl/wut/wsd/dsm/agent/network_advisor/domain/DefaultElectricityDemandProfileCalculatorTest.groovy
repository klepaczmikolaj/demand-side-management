package pl.wut.wsd.dsm.agent.network_advisor.domain


import pl.wut.wsd.dsm.agent.network_advisor.weather.model.BasicWeatherIndicators
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecastEntry
import spock.lang.Specification

import java.time.ZonedDateTime

class DefaultElectricityDemandProfileCalculatorTest extends Specification {

    private final int nHouses = 100
    private final int houseMaxConsumptionInWatts = 5000
    private final DefaultElectricityDemandProfileCalculator calculator = new DefaultElectricityDemandProfileCalculator(nHouses, houseMaxConsumptionInWatts)
    private static final ZonedDateTime fixedDate = ZonedDateTime.now()


    def "Should calculate demand"() {
        when:
        final result = calculator.calculate(forecast)

        then:
        result.measurements[fixedDate] == expected
        result.measurements[fixedDate.plusHours(1)] == expected1

        where:
        forecast | expected               | expected1
        new WeatherForecast(entries: [
                entry(fixedDate, 12),
                entry(fixedDate.plusHours(1), -20)
        ])       | new BigInteger(200000) | new BigInteger(520000)
    }

    def entry(final ZonedDateTime at, final double temperature) {
        return new WeatherForecastEntry(measurementDate: at, base: new BasicWeatherIndicators(
                temperature: temperature,
                minTemperature: temperature,
                maxTemperature: temperature
        ))
    }
}
