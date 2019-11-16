package pl.wut.wsd.dsm.agent;

import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;

public interface CustomerTrustFactorManager {
    void refreshTrustRanking();

    CustomerTrustRanking getCustomerTrustRanking(GetTrustRankingRequest request);
}
