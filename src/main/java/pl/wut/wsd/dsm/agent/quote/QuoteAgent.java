package pl.wut.wsd.dsm.agent.quote;

import jade.core.Agent;
import pl.wut.wsd.dsm.agent.QuoteManager;
import pl.wut.wsd.dsm.ontology.auction.CustomerObligation;

public class QuoteAgent extends Agent implements QuoteManager {

    @Override
    public void processClientDecision(final CustomerObligation customerObligation) {
        //TODO, process client offer
    }
}
