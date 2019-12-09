package pl.wut.wsd.dsm.agent.trust_factor;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.RankingReader;
import pl.wut.wsd.dsm.agent.trust_factor.ranking.TrustRankingRefresher;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;
import pl.wut.wsd.dsm.protocol.customer_trust.GetCustomerTrustProtocol;

import java.time.Duration;

@Slf4j
public class CustomerTrustAgent extends Agent {

    private Codec codec = Codec.json();
    private TrustRankingRefresher trustRankingRefresher;
    private RankingReader rankingReader;

    @Override
    protected void setup() {
        final CustomerTrustAgentDependencies dependencies = (CustomerTrustAgentDependencies) getArguments()[0];
        this.trustRankingRefresher = dependencies.getTrustRankingRefresher();
        this.rankingReader = dependencies.getRankingReader();

        addBehaviour(new MessageHandler(this,
                MessageSpecification.of(GetCustomerTrustProtocol.customerTrustRequest.toMessageTemplate(), this::handleRankingRequest)
        ));
        addBehaviour(new TickerBehaviour(this, Duration.ofSeconds(10).toMillis()) {
            @Override
            protected void onTick() {
                refreshRanking();
            }
        });
    }

    private void refreshRanking() {
        trustRankingRefresher.refresh();
    }

    private void handleRankingRequest(final ACLMessage message) {
        final Result<GetTrustRankingRequest, DecodingError> decoded = codec.decode(message.getContent(), GetCustomerTrustProtocol.customerTrustRequest.getMessageClass());

        if (!decoded.isValid()) {
            log.error("Could not decode", decoded.error());
            return;
        }

        final AID sender = message.getSender();
        final ACLMessage reply = GetCustomerTrustProtocol.customerTrustRankingResponse.templatedMessage();
        reply.addReceiver(sender);

        refreshRanking();
        final CustomerTrustRanking computedRanking = computeRanking(decoded.result());
        reply.setContent(codec.encode(computedRanking));

        send(reply);
    }

    private CustomerTrustRanking computeRanking(final GetTrustRankingRequest request) {
        return rankingReader.getRanking(request);
    }
}
