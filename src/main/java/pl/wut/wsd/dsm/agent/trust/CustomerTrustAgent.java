package pl.wut.wsd.dsm.agent.trust;

import jade.core.Agent;
import pl.wut.wsd.dsm.agent.CustomerTrustFactorManager;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;

public class CustomerTrustAgent extends Agent implements CustomerTrustFactorManager {

    @Override
    public void refreshTrustRanking() {

    }

    @Override
    public CustomerTrustRanking getCustomerTrustRanking(final GetTrustRankingRequest request) {
        return null; //TODO
    }

}
