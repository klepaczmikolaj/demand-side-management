package pl.wut.wsd.dsm.ontology.draft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumptionIncrease {
    private double availIncrease;
    private ZonedDateTime since;
    private ZonedDateTime until;
}
