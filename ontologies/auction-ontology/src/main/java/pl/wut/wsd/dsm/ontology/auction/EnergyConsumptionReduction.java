package pl.wut.wsd.dsm.ontology.auction;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class EnergyConsumptionReduction {
    private double notExceeding;
    private ZonedDateTime since;
    private ZonedDateTime until;
}
