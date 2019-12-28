package pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation;

import lombok.Data;

@Data
public class ObligationAcceptanceRequest {
    private Long relatedOfferId;
    private double kwsAccepted;
}
