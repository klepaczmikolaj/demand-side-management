package pl.wut.wsd.dsm.infrastructure.discovery;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ServiceDiscovery {

    private final Agent agent;

    public Result<List<DFAgentDescription>, FIPAException> findServices(final ServiceDescription serviceDescription) {
        final DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);
        return callYellowPagesService(dfAgentDescription);
    }

    private Result<List<DFAgentDescription>, FIPAException> callYellowPagesService(final DFAgentDescription dfAgentDescription) {
        try {
            final List<DFAgentDescription> agentIds = Arrays.stream(DFService.search(agent, dfAgentDescription))
                    .collect(Collectors.toList());
            return Result.ok(agentIds);
        } catch (final FIPAException e) {
            return Result.error(e);
        }
    }

}
