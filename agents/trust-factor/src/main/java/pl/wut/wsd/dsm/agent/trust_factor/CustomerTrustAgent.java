package pl.wut.wsd.dsm.agent.trust_factor;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.RankingReader;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Customer;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.repo.CustomerRepository;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.repo.TrustRankingRepository;
import pl.wut.wsd.dsm.agent.trust_factor.ranking.TrustRankingRefresher;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceRegistration;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;
import pl.wut.wsd.dsm.protocol.customer_trust.GetCustomerTrustProtocol;

import java.time.Duration;
import java.util.List;

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
        ensureAllCustomersHaveCorespondingEntry(dependencies.getCustomerRepository(), dependencies.getTrustRankingRepository());

        addBehaviour(new MessageHandler(this,
                MessageSpecification.of(GetCustomerTrustProtocol.customerTrustRequest.toMessageTemplate(), this::handleRankingRequest)
        ));
        addBehaviour(new TickerBehaviour(this, Duration.ofMinutes(1).toMillis()) {
            @Override
            protected void onTick() {
                refreshRanking();
            }
        });
        addBehaviour(new TickerBehaviour(this, Duration.ofMinutes(1).toMillis()) {
            @Override
            protected void onTick() {
                ensureAllCustomersHaveCorespondingEntry(dependencies.getCustomerRepository(), dependencies.getTrustRankingRepository());
            }
        });
        final ServiceRegistration serviceRegistration = new ServiceRegistration(this);
        serviceRegistration.registerRetryOnFailure(Duration.ofSeconds(5), GetCustomerTrustProtocol.customerTrustRequest.getTargetService());
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
        reply.setConversationId(message.getConversationId());

        refreshRanking();
        final CustomerTrustRanking computedRanking = computeRanking(decoded.result());
        reply.setContent(codec.encode(computedRanking));

        send(reply);
    }

    private CustomerTrustRanking computeRanking(final GetTrustRankingRequest request) {
        return rankingReader.getRanking(request);
    }

    private void ensureAllCustomersHaveCorespondingEntry(final CustomerRepository customerRepository, final TrustRankingRepository trustRepository) {
        final List<Customer> customers = customerRepository.findAllCustomers();
        customers.stream()
                .filter(c -> !trustRepository.getForCustomer(c).isPresent())
                .map(CustomerTrust::forCustomer)
                .forEach(trustRepository::save);
    }
}
