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
import pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest.RestClient;
import pl.wut.wsd.dsm.agent.network_advisor.weather.WeatherAdapter;

import java.time.Duration;

@Slf4j
public class NetworkAgentApplication {

    /* ------------------------------ PROPERTIES ------------------------------------------ */
    private static final String weatherForecastApiKey = "d250a33a932b1f3ea88676644df45ae3";
    private static final String weatherforecastCity = "Warsaw";
    private static final String weatherForecastCountryCode = "PL";
    private static final Duration weatherRefreshFrequency = Duration.ofSeconds(30);
    /* ------------------------------------------------------------------------------------- */

    public static void main(final String[] args) throws ControllerException {
        final RestClient restClient = RestClient.defaultClient();
        final WeatherAdapter weatherAdapter = WeatherAdapter.apiAdapter(restClient, weatherForecastApiKey, weatherforecastCity, weatherForecastCountryCode);

        final AgentContainer mainContainer = createAgentContainer();

        log.info("Starting network-agent");
        final NetworkAgentDependencies dependencies = NetworkAgentDependencies.builder()
                .weatherAdapter(weatherAdapter)
                .weatherRefreshDuration(weatherRefreshFrequency)
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
