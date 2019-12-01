package pl.wut.wsd.dsm.agent.network_advisor.weather;

import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;

public interface WeatherAdapter {

    WeatherForecast getWeatherForecast();
}
