package pl.wut.wsd.dsm.infrastructure.discovery;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.infrastructure.function.Result;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ServiceDiscovery<T extends Agent> {

    private final T agent;

    public Result<List<AID>, FIPAException> findServices(final ServiceDescription serviceDescription) {
        final DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);

        return callYellowPagesService(dfAgentDescription);
    }

    private Result<List<AID>, FIPAException> callYellowPagesService(final DFAgentDescription dfAgentDescription) {
        try {
            final List<AID> agentIds = Arrays.stream(DFService.search(agent, dfAgentDescription))
                    .map(DFAgentDescription::getName)
                    .collect(Collectors.toList());
            return Result.ok(agentIds);

        } catch (final FIPAException e) {
            return Result.error(e);
        }
    }

}
