package pl.wut.wsd.dsm.ontology.draft;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class EnergyConsumptionIncrease {
    private double notUnder;
    private ZonedDateTime since;
    private ZonedDateTime until;
}
