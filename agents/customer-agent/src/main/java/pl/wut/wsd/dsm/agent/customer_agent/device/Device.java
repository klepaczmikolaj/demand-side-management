package pl.wut.wsd.dsm.agent.customer_agent.device;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.device.DeviceBrief;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.device.DeviceType;

@Getter
public class Device extends DeviceBrief {
    private final DeviceType deviceType;
    private final double usage;
    @Setter
    private boolean isOn;
    @Setter
    private boolean includeInDsm;

    @Builder
    public Device(final Long id, final String name, final DeviceType deviceType, final double usage, final boolean isOn, final boolean includeInDsm) {
        super(id, name);
        this.deviceType = deviceType;
        this.usage = usage;
        this.isOn = isOn;
        this.includeInDsm = includeInDsm;
    }
}
