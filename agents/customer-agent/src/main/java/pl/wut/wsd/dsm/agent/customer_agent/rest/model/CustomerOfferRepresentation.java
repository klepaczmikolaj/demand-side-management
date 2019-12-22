package pl.wut.wsd.dsm.agent.customer_agent.rest.model;

import lombok.Data;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.device.DeviceSwitchRecommendation;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerOfferRepresentation {
    private UUID offerId;
    private double sizeKws;
    private ObligationType type;
    private double amountPerKWh;
    private double notExceeding;
    private ZonedDateTime offerEndDateTime;
    private List<DeviceSwitchRecommendation> devicesToTurnOff;
    private List<DeviceSwitchRecommendation> devicesToTurnOn;
    private ZonedDateTime demandChangeStart;
    private ZonedDateTime demandChangeEnd;
}
