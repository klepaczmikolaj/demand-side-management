package pl.wut.wsd.dsm.agent.network_advisor.domain

import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.InterpolatedElectricityProductionProfile
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.Cloudiness
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecastEntry
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.Wind
import spock.lang.Specification

import java.time.ZonedDateTime

class DefaultElectricityProductionProfileCalculatorTest extends Specification {

    private static final double solarPower = 10
    private static final double windPower = 20
    private final static ZonedDateTime fixedDate = ZonedDateTime.now()

    private final DefaultElectricityProductionProfileCalculator calculator =
            new DefaultElectricityProductionProfileCalculator(solarPower, windPower)

    def 'Should prepare profile'() {
        given:
        final WeatherForecast forecast = new WeatherForecast(entries: [
                new WeatherForecastEntry(measurementDate: fixedDate, cloudiness: new Cloudiness(percentage: 50), wind: new Wind(speed: 15)),
                new WeatherForecastEntry(measurementDate: fixedDate.plusHours(1), cloudiness: new Cloudiness(percentage: 100), wind: new Wind(speed: 0))
        ])

        when:
        final InterpolatedElectricityProductionProfile result = calculator.calculate(forecast)

        then:
        result.measurements.size() == 2
        result.measurements[fixedDate] == BigInteger.valueOf(15)
        result.measurements[fixedDate.plusHours(1)] == BigInteger.ZERO

    }
}
