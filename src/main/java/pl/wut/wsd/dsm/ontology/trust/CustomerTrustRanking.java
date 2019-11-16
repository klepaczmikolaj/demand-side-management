package pl.wut.wsd.dsm.ontology.trust;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "ofEntries")
public class CustomerTrustRanking {
    private final List<CustomerTrustRankingEntry> rankingEntries;
}
