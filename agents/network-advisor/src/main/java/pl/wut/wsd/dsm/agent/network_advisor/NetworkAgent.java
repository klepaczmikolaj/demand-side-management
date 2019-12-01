package pl.wut.wsd.dsm.agent.network_advisor;

import jade.core.Agent;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;

public class NetworkAgent extends Agent {

    private NetworkAgentDependencies dependencies;

    @Override
    protected void setup() {
        dependencies = (NetworkAgentDependencies) this.getArguments()[0];
        WeatherForecaster.bindToAgent(this, dependencies.weatherAdapter(), dependencies.weatherRefreshDuration());
        this.subscribeToNetworkData();
    }

    private void subscribeToNetworkData() {
        //TODO monitor network
    }

    /**
     * Informs quote manager of expected inbalancement withing microgrid.
     */
    private void inform(final ExpectedInbalancement expectedInbalancement) {
        //TODO
    }


}
