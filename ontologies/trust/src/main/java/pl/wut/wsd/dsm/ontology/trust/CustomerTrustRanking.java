package pl.wut.wsd.dsm.ontology.trust;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "ofEntries")
public class CustomerTrustRanking {
    private final List<CustomerTrustRankingEntry> rankingEntries;
}
