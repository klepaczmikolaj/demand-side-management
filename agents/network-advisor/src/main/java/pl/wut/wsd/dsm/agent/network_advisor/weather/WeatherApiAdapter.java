package pl.wut.wsd.dsm.agent.network_advisor.weather;

import pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest.RestClient;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;

import java.util.HashMap;
import java.util.Map;

class WeatherApiAdapter implements WeatherAdapter {

    private final RestClient restClient = RestClient.defaultClient();
    private static final String apiKey = "d250a33a932b1f3ea88676644df45ae3";

    private final Map<String, String> queryParams;

    public WeatherApiAdapter() {
        this.queryParams = new HashMap<>();
        queryParams.put("appid", apiKey);
        queryParams.put("q", "Warsaw,pl");
        queryParams.put("units", "metric");
    }

    @Override
    public WeatherForecast getWeatherForecast() {
        return restClient.get("http://api.openweathermap.org/data/2.5/forecast", WeatherForecast.class, queryParams);
    }


}
