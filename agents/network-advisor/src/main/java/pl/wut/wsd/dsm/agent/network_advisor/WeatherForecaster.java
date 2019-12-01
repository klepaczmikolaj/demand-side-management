package pl.wut.wsd.dsm.agent.network_advisor;

import jade.core.behaviours.TickerBehaviour;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.network_advisor.weather.WeatherAdapter;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;

import java.time.Duration;

@Slf4j
public class WeatherForecaster extends TickerBehaviour {

    private final WeatherAdapter weatherAdapter;

    @Getter
    private WeatherForecast forecast;

    public static void bindToAgent(final NetworkAgent agent, final WeatherAdapter weatherAdapter, final Duration weatherRefreshFrequency) {
        final WeatherForecaster forecaster = new WeatherForecaster(weatherRefreshFrequency, weatherAdapter, agent);
        agent.addBehaviour(forecaster);
    }

    private WeatherForecaster(final Duration weatherRefreshFrequency, final WeatherAdapter weatherAdapter, final NetworkAgent a) {
        super(a, weatherRefreshFrequency.toMillis());
        this.weatherAdapter = weatherAdapter;
        this.myAgent = a;
        this.forecast = this.weatherAdapter.getWeatherForecast();
    }

    @Override
    protected void onTick() {
        updateForecast();
    }

    private void updateForecast() {
        try {
            this.forecast = weatherAdapter.getWeatherForecast();
            log.info("Forecast refreshed");
        } catch (final Exception e) {
            log.error("Could not update forecast", e);
        }
    }
}
