package pl.wut.wsd.dsm.ontology.trust;

import lombok.Data;

@Data
public class GetTrustRankingRequest {
    private int from;
    private int to;
    private SelectionType selectionType;

    public enum SelectionType {
        ROULETTE, REGULAR
    }
}
