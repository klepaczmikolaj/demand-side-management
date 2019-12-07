package pl.wut.wsd.dsm.agent.customer_agent.rest.model;

import lombok.Data;

@Data
public class ElectricDevice {
    private Long id;
    private String name;
    private DeviceType deviceType;
    private double usage;
    private boolean isOn;
    private String includeInDsm;
}
