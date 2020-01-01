package pl.wut.wsd.dsm.ontology.trust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTrustRankingRequest {
    private int from;
    private int to;
    private SelectionType selectionType;

    public enum SelectionType {
        ROULETTE, REGULAR
    }

    public static GetTrustRankingRequest requestWholeRanking() {
        return new GetTrustRankingRequest(0, Integer.MAX_VALUE, SelectionType.REGULAR);
    }
}
