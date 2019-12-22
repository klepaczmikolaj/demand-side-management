package pl.wut.wsd.dsm.infrastructure.messaging.handle;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.infrastructure.common.function.CollectionTransformer;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
class DefaultMessagingCapability implements AgentMessagingCapability {
    private final ServiceDiscovery discovery;
    private final Agent agent;

    @Override
    public Result<Set<AID>, FIPAException> send(final ACLMessage aclMessage, final ServiceDescription serviceDescription) {
        final Result<List<DFAgentDescription>, FIPAException> searchResult = discovery.findServices(serviceDescription);

        if (searchResult.isError()) {
            return Result.error(searchResult.error());
        }

        return searchResult.mapResult(list -> {
            final List<AID> aids = CollectionTransformer.mapToList(list, DFAgentDescription::getName);
            aids.forEach(aclMessage::addReceiver);

            return new HashSet<>(aids);
        });

    }

}
