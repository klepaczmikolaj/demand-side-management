package pl.wut.wsd.dsm.ontology.network;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DraftSummary {
    private UUID id;
    private ZonedDateTime since;
    private ZonedDateTime until;
    private double reductionObligationsSum;
    private double increaseObligationsSum;
}
