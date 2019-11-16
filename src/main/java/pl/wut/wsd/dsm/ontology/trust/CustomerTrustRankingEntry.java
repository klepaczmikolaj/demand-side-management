package pl.wut.wsd.dsm.ontology.trust;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.wut.wsd.dsm.ontology.Customer;

@Getter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class CustomerTrustRankingEntry {
    private final int rank;
    private final Customer customer;
    private final double trustCoefficentValue;
}
