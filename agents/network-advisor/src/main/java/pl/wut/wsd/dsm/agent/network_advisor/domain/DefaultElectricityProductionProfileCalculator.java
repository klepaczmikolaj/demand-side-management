package pl.wut.wsd.dsm.agent.network_advisor.domain;

import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.ElectricityProductionProfile;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecastEntry;

import java.math.BigInteger;

public class DefaultElectricityProductionProfileCalculator implements ElectricityProductionProfileCalculator {

    @Override
    public ElectricityProductionProfile calculate(final WeatherForecast weatherForecast) {
        for (WeatherForecastEntry entry : weatherForecast.getEntries()) {
            entry.getMeasurementDate()
        }
        return time -> BigInteger.valueOf(50L);
    }
}
