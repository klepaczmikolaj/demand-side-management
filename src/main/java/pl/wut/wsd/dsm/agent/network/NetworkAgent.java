package pl.wut.wsd.dsm.agent.network;

import jade.core.Agent;
import pl.wut.wsd.dsm.agent.NetworkAdvisor;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;

public class NetworkAgent extends Agent implements NetworkAdvisor {

    @Override
    protected void setup() {
        this.subscribeToNetworkData();
    }

    private void subscribeToNetworkData() {
        //TODO monitor network
    }


    @Override
    public void inform(final ExpectedInbalancement expectedInbalancement) {
        //TODO
    }
}
