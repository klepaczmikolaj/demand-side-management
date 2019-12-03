package pl.wut.wsd.dsm.ontology.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedInbalancement {
    private ZonedDateTime since;
    private ZonedDateTime until;
    private DemandAndProduction expectedDemandAndProduction;
}
