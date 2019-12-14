package pl.wut.wsd.dsm.infrastructure.discovery;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ServiceRegistration {

    private final Agent agent;

    public Optional<FIPAException> register(final ServiceDescription... serviceDescriptions) {
        final DFAgentDescription description = new DFAgentDescription();
        description.setName(agent.getAID());
        final List<ServiceDescription> services = Arrays.asList(serviceDescriptions);
        services.forEach(description::addServices);

        try {
            DFService.register(agent, description);
            log.info("Properly registered agent {} to service registry", agent.getAID());
            return Optional.empty();
        } catch (final FIPAException e) {
            return Optional.of(e);
        }
    }

    public void registerRetryOnFailure(final Duration retryInterval, final ServiceDescription... serviceDescriptions) {
        final Optional<FIPAException> maybeException = register(serviceDescriptions);

        if (maybeException.isPresent()) {
            log.error("Could not register to whitepages, retry in: " + retryInterval, maybeException.get());
            agent.addBehaviour(new WakerBehaviour(agent, retryInterval.toMillis()) {
                @Override
                protected void onWake() {
                    registerRetryOnFailure(retryInterval, serviceDescriptions);
                }
            });
        }
    }
}
