package pl.wut.wsd.dsm.agent.network_advisor.domain;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.ElectricityProductionProfile;
import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.InterpolatedElectricityProductionProfile;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.Cloudiness;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecastEntry;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.Wind;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultElectricityProductionProfileCalculator implements ElectricityProductionProfileCalculator {

    private final double solarPower;
    private final double windPower;

    @Override
    public ElectricityProductionProfile calculate(final WeatherForecast weatherForecast) {
        final Map<ZonedDateTime, BigInteger> productionPoints = weatherForecast.getEntries().stream()
                .collect(Collectors.toMap(WeatherForecastEntry::getMeasurementDate, this::calculate));

        return new InterpolatedElectricityProductionProfile(productionPoints);
    }

    /**
     * Speed m/s, pewnie od 0 do 30
     */
    private BigInteger calculate(final WeatherForecastEntry entry) {
        return windPowerProduction(entry.getWind()).add(solarProduction(entry.getCloudiness(), entry.getMeasurementDate()));
    }

    private BigInteger solarProduction(final Cloudiness cloudiness, final ZonedDateTime at) {
        if (at.getHour() < 7 || at.getHour() > 19)
            return BigInteger.ZERO;

        return Optional.ofNullable(cloudiness)
                .map(Cloudiness::getPercentage)
                .map(percentage -> solarPower * (1 - percentage / 100))
                .map(Double::longValue)
                .map(BigInteger::valueOf)
                .orElseGet(() -> BigInteger.valueOf((long) solarPower / 5));

    }

    private BigInteger windPowerProduction(final Wind wind) {
        final double speed = wind.getSpeed();
        if (speed < 1 || speed > 30) {
            return BigInteger.ZERO;
        }

        return BigInteger.valueOf((long) (windPower * wind.getSpeed() / 30));
    }
}
