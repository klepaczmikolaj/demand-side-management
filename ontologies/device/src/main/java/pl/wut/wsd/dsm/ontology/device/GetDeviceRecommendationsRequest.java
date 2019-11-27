package pl.wut.wsd.dsm.ontology.device;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GetDeviceRecommendationsRequest {
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private double targetConsumptionWatts;
}
