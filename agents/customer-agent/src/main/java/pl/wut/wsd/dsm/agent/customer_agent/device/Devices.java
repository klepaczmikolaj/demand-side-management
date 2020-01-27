package pl.wut.wsd.dsm.agent.customer_agent.device;

import lombok.Getter;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.device.DeviceType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Devices {

    public Optional<Device> findById(final Long id) {
        return devices.stream().filter(d -> d.getId().equals(id)).findFirst();
    }

    @Getter
    private final List<Device> devices = Arrays.asList(
            Device.builder().id(1L)
                    .name("Pralkens")
                    .deviceType(DeviceType.WashingMachine)
                    .includeInDsm(true)
                    .isOn(true)
                    .usage(350)
                    .build(),
            Device.builder().id(2L)
                    .name("Lodówa")
                    .includeInDsm(true)
                    .deviceType(DeviceType.Fridge)
                    .isOn(true)
                    .usage(200)
                    .build(),
            Device.builder().id(3L)
                    .name("Suszarka")
                    .includeInDsm(true)
                    .deviceType(DeviceType.Oven)
                    .isOn(true)
                    .usage(1500)
                    .build(),
            Device.builder().id(1L)
                    .name("Telewizor")
                    .deviceType(DeviceType.TV)
                    .includeInDsm(true)
                    .isOn(true)
                    .usage(100)
                    .build(),
            Device.builder().id(4L)
                    .name("Computer")
                    .deviceType(DeviceType.Computer)
                    .includeInDsm(true)
                    .isOn(true)
                    .usage(60)
                    .build()
    );

    public double getCurrentUsageInWatts() {
        return this.devices.stream().filter(Device::isOn).mapToDouble(Device::getUsage).sum();
    }

}
