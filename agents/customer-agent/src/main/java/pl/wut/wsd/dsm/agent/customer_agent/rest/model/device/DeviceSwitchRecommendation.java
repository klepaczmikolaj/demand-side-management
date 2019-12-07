package pl.wut.wsd.dsm.agent.customer_agent.rest.model.device;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class DeviceSwitchRecommendation {
    private DeviceBrief deviceBrief;
    private ZonedDateTime since;
    private ZonedDateTime until;
    private double kilowatts;
}
