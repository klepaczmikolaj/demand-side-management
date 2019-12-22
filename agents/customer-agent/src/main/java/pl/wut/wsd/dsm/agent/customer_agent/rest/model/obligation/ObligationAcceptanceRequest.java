package pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation;

import lombok.Data;

import java.util.UUID;

@Data
public class ObligationAcceptanceRequest {
    private UUID relatedOfferId;
    private double kwsAccepted;
}
