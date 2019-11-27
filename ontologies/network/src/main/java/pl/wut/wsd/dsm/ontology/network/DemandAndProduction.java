package pl.wut.wsd.dsm.ontology.network;

import lombok.Data;

import java.math.BigInteger;

@Data
public class DemandAndProduction {
    private BigInteger wattsDemand;
    private BigInteger wattsProduction;
}
