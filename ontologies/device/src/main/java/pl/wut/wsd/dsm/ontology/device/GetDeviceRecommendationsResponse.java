package pl.wut.wsd.dsm.ontology.device;

import lombok.Data;

import java.util.Set;

@Data
public class GetDeviceRecommendationsResponse {
    private Set<DeviceRecommendation> deviceRecommendations;
}
