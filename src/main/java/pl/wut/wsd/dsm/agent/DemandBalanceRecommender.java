package pl.wut.wsd.dsm.agent;

import pl.wut.wsd.dsm.ontology.device.GetDeviceRecommendationsRequest;
import pl.wut.wsd.dsm.ontology.device.GetDeviceRecommendationsResponse;

public interface DemandBalanceRecommender {
    GetDeviceRecommendationsResponse getDeviceRecommendations(GetDeviceRecommendationsRequest request);
}
