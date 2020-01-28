package pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation;

import lombok.Data;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ObligationRepresentation {
    private boolean available;
    private UUID relatedOfferId;
    private double obligationSizeKw;
    private ZonedDateTime since;
    private ZonedDateTime until;
    private ObligationType type;
    private String state;
    private Double notExceedingKws;
    private Double notBelowKws;
}
