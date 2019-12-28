package pl.wut.wsd.dsm.agent.network_advisor.weather;

import pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest.RestClient;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;

import java.util.HashMap;
import java.util.Map;

class WeatherApiAdapter implements WeatherAdapter {

    private final RestClient restClient;
    private final String apiKey;

    private final Map<String, String> queryParams;

    public WeatherApiAdapter(final RestClient restClient, final String apiKey, final String weatherForecastCityAndCountryCode) {
        this.restClient = restClient;
        this.apiKey = apiKey;
        this.queryParams = new HashMap<>();
        queryParams.put("appid", this.apiKey);
        queryParams.put("q", weatherForecastCityAndCountryCode);
        queryParams.put("units", "metric");
    }

    @Override
    public WeatherForecast getWeatherForecast() {
        return restClient.get("http://api.openweathermap.org/data/2.5/forecast", WeatherForecast.class, queryParams);
    }


}
