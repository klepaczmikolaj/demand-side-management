package pl.wut.wsd.dsm.ontology.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandAndProduction {
    private double wattsDemand;
    private double wattsProduction;
}
