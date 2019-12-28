package pl.wut.wsd.dsm.ontology.device;

import lombok.Data;

import java.util.UUID;

@Data
public class ElectricDevice {
    private UUID id;
    private int priorityLevel;
    private double currentConsumptionWatts;
}
