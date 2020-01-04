package pl.wut.wsd.dsm.agent.network_advisor.domain;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.ElectricityDemandProfile;
import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.InterpolatedElectricityDemandProfile;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecastEntry;
import pl.wut.wsd.dsm.ontology.network.DraftSummary;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultElectricityDemandProfileCalculator implements ElectricityDemandProfileCalculator {

    private final Map<UUID, DraftSummary> draft = new HashMap<>();
    private final int houses;
    private final double houseMaxConsumptionInWatts;

    @Override
    public ElectricityDemandProfile calculate(final WeatherForecast weatherForecast) {
        final Map<ZonedDateTime, BigInteger> demandByDate = weatherForecast.getEntries().stream()
                .collect(Collectors.toMap(WeatherForecastEntry::getMeasurementDate, this::computeDemand));

        return new InterpolatedElectricityDemandProfile(demandByDate);
    }

    @Override
    public void registerDraft(final DraftSummary draftSummary) {
        this.draft.put(draftSummary.getId(), draftSummary);
    }

    private BigInteger computeDemand(final WeatherForecastEntry entry) {
        final double temperature = entry.getBase().getTemperature();

        final double constantDemand = 0.4 * houses * houseMaxConsumptionInWatts;
        final double demandFromTemperature = temperature > 12 ? 0 : Math.abs(12 - temperature) / 50; // temperature ranges from -20 to 30
        final double demandFromDrafts = draft.values().stream()
                .filter(containsDate(entry.getMeasurementDate()))
                .mapToDouble(d -> d.getIncreaseObligationsSum() - d.getReductionObligationsSum())
                .sum();

        return BigInteger.valueOf(Double.valueOf(demandFromTemperature * houses * houseMaxConsumptionInWatts + demandFromDrafts + constantDemand).longValue());
    }

    private Predicate<DraftSummary> containsDate(final ZonedDateTime time) {
        return d -> (d.getSince().isBefore(time) || d.getSince().isEqual(time))
                && (d.getUntil().isAfter(time) || d.getUntil().isEqual(time));
    }
}