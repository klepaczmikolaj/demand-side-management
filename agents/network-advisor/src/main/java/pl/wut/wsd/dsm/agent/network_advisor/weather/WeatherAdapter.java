package pl.wut.wsd.dsm.agent.network_advisor.weather;

import pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest.RestClient;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;

public interface WeatherAdapter {

    WeatherForecast getWeatherForecast();

    static WeatherAdapter apiAdapter(final RestClient restClient, final String apiKey, final String city, final String countryCode) {
        return new WeatherApiAdapter(restClient, apiKey, city + "," + countryCode.toLowerCase());
    }
}
