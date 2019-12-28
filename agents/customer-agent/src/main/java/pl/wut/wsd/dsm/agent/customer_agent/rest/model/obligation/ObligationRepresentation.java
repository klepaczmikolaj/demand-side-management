package pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ObligationRepresentation {
    private boolean available;
    private Long relatedOfferId;
    private double obligationSizeKw;
    private ZonedDateTime since;
    private ZonedDateTime until;
}
