package pl.wut.wsd.dsm.agent.network_advisor.domain;

import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.ElectricityDemandProfile;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;
import pl.wut.wsd.dsm.ontology.network.DraftSummary;

public interface ElectricityDemandProfileCalculator {
    ElectricityDemandProfile calculate(final WeatherForecast weatherForecast);

    void registerDraft(DraftSummary draftSummary);
}
