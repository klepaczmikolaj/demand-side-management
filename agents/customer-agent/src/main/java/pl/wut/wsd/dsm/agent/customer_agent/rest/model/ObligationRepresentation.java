package pl.wut.wsd.dsm.agent.customer_agent.rest.model;

import lombok.Data;

@Data
class ObligationRepresentation {
    private boolean available;
    private Long relatedOfferId;
    private double obligationSizeKw;
}
