package pl.wut.wsd.dsm.ontology.network;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class DraftSummary {
    private ZonedDateTime since;
    private ZonedDateTime until;
    private double reductionObligationsSum;
    private double increaseObligationsSum;
}
