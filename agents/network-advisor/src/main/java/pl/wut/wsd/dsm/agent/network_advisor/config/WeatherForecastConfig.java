package pl.wut.wsd.dsm.agent.network_advisor.config;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class WeatherForecastConfig {
    private final String weatherForecastApiKey;
    private final String weatherforecastCity;
    private final String weatherForecastCountryCode;
    private final Duration weatherRefreshFrequency;
}
