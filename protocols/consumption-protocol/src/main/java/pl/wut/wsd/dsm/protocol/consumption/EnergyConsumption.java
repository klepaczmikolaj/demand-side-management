package pl.wut.wsd.dsm.protocol.consumption;

import lombok.Data;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Data
public class EnergyConsumption {
    @NonNull
    private ZonedDateTime measuredAt;

    @NonNull
    private double wattsConsumption;
}
