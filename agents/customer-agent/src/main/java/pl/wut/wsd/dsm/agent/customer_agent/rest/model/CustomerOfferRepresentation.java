package pl.wut.wsd.dsm.agent.customer_agent.rest.model;

import lombok.Data;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.device.DeviceSwitchRecommendation;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerOfferRepresentation {
    private UUID offerId;
    private double sizeKws;
    private ObligationType type;
    private boolean available;
    private double amountPerKWh;
    private LocalDateTime offerEndDateTime;
    private List<DeviceSwitchRecommendation> devicesToTurnOff;
    private List<DeviceSwitchRecommendation> devicesToTurnOn;
    private LocalDateTime demandChangeStart;
    private LocalDateTime demandChangeEnd;
}
