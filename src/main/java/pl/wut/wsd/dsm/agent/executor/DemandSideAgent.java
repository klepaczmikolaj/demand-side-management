package pl.wut.wsd.dsm.agent.executor;

import jade.core.Agent;
import pl.wut.wsd.dsm.agent.DemandBalanceRecommender;
import pl.wut.wsd.dsm.agent.Executor;
import pl.wut.wsd.dsm.ontology.device.GetDeviceRecommendationsRequest;
import pl.wut.wsd.dsm.ontology.device.GetDeviceRecommendationsResponse;


public class DemandSideAgent extends Agent implements Executor, DemandBalanceRecommender {

    @Override
    public GetDeviceRecommendationsResponse getDeviceRecommendations(final GetDeviceRecommendationsRequest request) {
        return null; //TODO
    }
}
