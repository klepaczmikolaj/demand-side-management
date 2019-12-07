package pl.wut.wsd.dsm.agent.customer_agent.rest.model;

import pl.wut.wsd.dsm.agent.customer_agent.rest.model.device.DeviceSwitchRecommendation;

import java.time.ZonedDateTime;
import java.util.List;

public class CustomerOfferRepresentation {
    private Long offerId;
    private double reducedKws;
    private double amountPerKWh;
    private ZonedDateTime offerEndDateTime;
    private List<DeviceSwitchRecommendation> devicesToTurnOff;
    private List<DeviceSwitchRecommendation> devicesToTurnOn;
    private ZonedDateTime demandChangeStart;
    private ZonedDateTime demandChangeEnd;
}
