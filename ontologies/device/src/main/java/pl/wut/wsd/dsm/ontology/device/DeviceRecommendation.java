package pl.wut.wsd.dsm.ontology.device;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class DeviceRecommendation {
    private ElectricDevice electricDevice;
    private ZonedDateTime from;
    private ZonedDateTime until;

    private RecommendationType recommendationType;

    public enum RecommendationType {
        SWITCH_ON, SWITCH_OFF
    }
}
