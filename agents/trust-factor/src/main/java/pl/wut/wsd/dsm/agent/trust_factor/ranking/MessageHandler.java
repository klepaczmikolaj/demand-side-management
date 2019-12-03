package pl.wut.wsd.dsm.agent.trust_factor.ranking;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageHandler extends CyclicBehaviour {

    private static final int rankingRequestPerformative = ACLMessage.QUERY_REF;
    private static final int refreshRankingInformation = ACLMessage.INFORM;

    @Override
    public void action() {
        final ACLMessage received = myAgent.receive();
        if (received != null) {

        }
        // zapytanie o aktualny ranking
    }

}
