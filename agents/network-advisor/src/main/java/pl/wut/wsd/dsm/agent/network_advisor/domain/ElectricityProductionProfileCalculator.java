package pl.wut.wsd.dsm.agent.network_advisor.domain;

import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.ElectricityProductionProfile;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;

public interface ElectricityProductionProfileCalculator {
    ElectricityProductionProfile calculate(final WeatherForecast weatherForecast);
}
