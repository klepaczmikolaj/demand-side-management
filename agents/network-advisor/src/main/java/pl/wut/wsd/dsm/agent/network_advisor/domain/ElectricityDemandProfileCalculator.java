package pl.wut.wsd.dsm.agent.network_advisor.domain;

import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.ElectricityDemandProfile;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;

public interface ElectricityDemandProfileCalculator {
    ElectricityDemandProfile calculate(final WeatherForecast weatherForecast);
}
