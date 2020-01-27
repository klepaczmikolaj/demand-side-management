package pl.wut.wsd.dsm.protocol.consumption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumption {
    @NonNull
    private ZonedDateTime measuredAt;

    @NonNull
    private double wattsConsumption;
}
