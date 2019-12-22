package pl.wut.wsd.dsm.ontology.draft;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerObligation {
    private UUID relatedOfferId;
    private ObligationType obligationType;
    private double kwsChange;
}