package pl.wut.wsd.dsm.ontology.draft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOffer {
    private UUID offerId;
    private EnergyConsumptionReduction energyConsumptionReduction;
    private EnergyConsumptionIncrease energyConsumptionIncrease;
    private BigDecimal pricePerKw;
    private ZonedDateTime validUntil;
}
