package pl.wut.wsd.dsm.agent;

import pl.wut.wsd.dsm.ontology.auction.CustomerObligation;

public interface QuoteManager {

    void processClientDecision(final CustomerObligation customerObligation);
}
