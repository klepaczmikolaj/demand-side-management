package pl.wut.wsd.dsm.ontology.network;

import lombok.Data;
import pl.wut.wsd.dsm.mocks.DemandAndProduction;

import java.time.ZonedDateTime;

@Data
public class ExpectedInbalancement {
    private ZonedDateTime since;
    private ZonedDateTime until;
    private DemandAndProduction expectedDemandAndProduction;
}
