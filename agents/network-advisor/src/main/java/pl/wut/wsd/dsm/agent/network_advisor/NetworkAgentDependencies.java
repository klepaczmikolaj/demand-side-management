package pl.wut.wsd.dsm.agent.network_advisor;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import pl.wut.wsd.dsm.agent.network_advisor.domain.ElectricityDemandProfileCalculator;
import pl.wut.wsd.dsm.agent.network_advisor.domain.ElectricityProductionProfileCalculator;
import pl.wut.wsd.dsm.agent.network_advisor.weather.WeatherAdapter;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

import java.time.Duration;

@Accessors(fluent = true)
@Getter
@Builder
public class NetworkAgentDependencies {
    private final WeatherAdapter weatherAdapter;
    private final Duration weatherRefreshDuration;
    private final Duration demandProfileRefreshFrequency;
    private final Duration productionProfileRefreshFrequency;
    private final Duration inbalancementCheckRefreshFrequency;
    private final Duration inbalancementRefreshAdvancement;
    private final double safetyTresholdWatts;
    private final Codec codec;
    private final ElectricityProductionProfileCalculator electricityProductionProfileCalculator;
    private final ElectricityDemandProfileCalculator electricityDemandProfileCalculator;
}
