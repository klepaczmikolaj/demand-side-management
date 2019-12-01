package pl.wut.wsd.dsm.agent.network_advisor;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import pl.wut.wsd.dsm.agent.network_advisor.weather.WeatherAdapter;

import java.time.Duration;

@Accessors(fluent = true)
@Getter
@Builder
public class NetworkAgentDependencies {
    private final WeatherAdapter weatherAdapter;
    private final Duration weatherRefreshDuration;
}
