package pl.wut.wsd.dsm.agent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.network_advisor.NetworkAgent;
import pl.wut.wsd.dsm.agent.network_advisor.NetworkAgentDependencies;
import pl.wut.wsd.dsm.agent.network_advisor.domain.ElectricityDemandProfileCalculator;
import pl.wut.wsd.dsm.agent.network_advisor.domain.ElectricityProductionProfileCalculator;
import pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest.RestClient;
import pl.wut.wsd.dsm.agent.network_advisor.weather.WeatherAdapter;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

import java.math.BigInteger;
import java.time.Duration;

@Slf4j
public class NetworkAgentApplication {

    /* ------------------------------ PROPERTIES ------------------------------------------ */
    private static final String weatherForecastApiKey = "d250a33a932b1f3ea88676644df45ae3";
    private static final String weatherforecastCity = "Warsaw";
    private static final String weatherForecastCountryCode = "PL";
    private static final Duration weatherRefreshFrequency = Duration.ofSeconds(60);
    private static final Duration demandProfileRefreshFrequency = Duration.ofMinutes(5);
    private static final Duration productionProfileRefreshFrequency = Duration.ofMinutes(5);
    private static final Duration inbalancementCheckRefresFrequency = Duration.ofMinutes(5);
    private static final double safetyTreshold = 50;
    private static final ElectricityDemandProfileCalculator demandCalc = forecast -> time -> BigInteger.TEN;
    private static final ElectricityProductionProfileCalculator productionCalc = forecast -> time -> BigInteger.valueOf(50L);
    /* ------------------------------------------------------------------------------------- */

    public static void main(final String[] args) throws ControllerException {
        final RestClient restClient = RestClient.defaultClient();
        final WeatherAdapter weatherAdapter = WeatherAdapter.apiAdapter(restClient, weatherForecastApiKey, weatherforecastCity, weatherForecastCountryCode);

        final AgentContainer mainContainer = createAgentContainer();

        log.info("Starting network-agent");
        final NetworkAgentDependencies dependencies = NetworkAgentDependencies.builder()
                .weatherAdapter(weatherAdapter)
                .weatherRefreshDuration(weatherRefreshFrequency)
                .demandProfileRefreshFrequency(demandProfileRefreshFrequency)
                .productionProfileRefreshFrequency(productionProfileRefreshFrequency)
                .inbalancementCheckRefreshFrequency(inbalancementCheckRefresFrequency)
                .safetyTresholdWatts(safetyTreshold)
                .electricityDemandProfileCalculator(demandCalc)
                .electricityProductionProfileCalculator(productionCalc)
                .codec(Codec.json())
                .build();

        final AgentController agentController = mainContainer.createNewAgent("network-agent", NetworkAgent.class.getCanonicalName(), new Object[]{dependencies});
        agentController.start();
        log.info("All started");
    }

    private static AgentContainer createAgentContainer() {
        final Runtime runtime = Runtime.instance();
        final Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "wsd-dsm");
        profile.setParameter(Profile.CONTAINER_NAME, "main-container");
        profile.setParameter("gui", Boolean.toString(true));
        return runtime.createMainContainer(profile);
    }
}
