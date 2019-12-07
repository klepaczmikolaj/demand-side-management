package pl.wut.wsd.dsm.agent.gateway;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.infrastructure.JadeIteratorUtil;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
public class GatewayAgent extends Agent {

    private static final String customerApiAddress = "customerApiAddress";

    private List<DFAgentDescription> customerAgents = new ArrayList<>();
    private final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);
    private GatewayAgentDelegate delegate;

    @Override
    protected void setup() {
        addBehaviour(new TickerBehaviour(this, Duration.ofSeconds(5).toMillis()) {
            @Override
            protected void onTick() {
                final DFAgentDescription dfAgentDescription = new DFAgentDescription();
                final ServiceDescription serviceDescription = new ServiceDescription();
                dfAgentDescription.addServices(serviceDescription);
                serviceDescription.setName("customer-agent");

                final Result<List<DFAgentDescription>, FIPAException> result = serviceDiscovery.findServices(serviceDescription);

                if (result.isValid()) {
                    customerAgents = result.result();
                    log.info("Found: {} customer agents", result.result().size());
                } else {
                    log.error("Could not load customer agents", result.error());
                }
            }
        });
        delegate = (GatewayAgentDelegate) getArguments()[0];
        delegate.gatewayAgent = this;
    }

    public Optional<URL> findCustomerUrl(final Long customerId) {
        return customerAgents.stream()
                .filter(isCustomerHandler(customerId))
                .map(JadeIteratorUtil::getAllServices)
                .flatMap(Collection::stream)
                .map(JadeIteratorUtil::getProperties)
                .flatMap(Collection::stream)
                .filter(p -> p.getName().equals(customerApiAddress))
                .map(Property::getValue)
                .map(Object::toString)
                .map(this::toUrl)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<URL> toUrl(final String s) {
        try {
            return Optional.of(new URL(s));
        } catch (final MalformedURLException e) {
            log.error("Cannot parse url: " + s, e);
            return Optional.empty();
        }
    }

    private Predicate<DFAgentDescription> isCustomerHandler(final Long customerId) {
        return description -> JadeIteratorUtil.getAllServices(description).stream()
                .filter(s -> s.getName().equals("customer-agent"))
                .map(JadeIteratorUtil::getProperties)
                .flatMap(Collection::stream)
                .anyMatch(p -> p.getName().equals("customerId") && p.getValue().toString().equals(customerId.toString()));

    }
}
