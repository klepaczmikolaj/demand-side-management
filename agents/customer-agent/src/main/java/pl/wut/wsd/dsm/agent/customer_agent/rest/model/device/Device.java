package pl.wut.wsd.dsm.agent.customer_agent.rest.model.device;

import lombok.Data;

@Data
public class Device extends DeviceBrief {
    private DeviceType deviceType;
    private double usage;
    private boolean isOn;
    private String includeInDsm;
}
