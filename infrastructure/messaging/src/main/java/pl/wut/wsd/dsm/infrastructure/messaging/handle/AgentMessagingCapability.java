package pl.wut.wsd.dsm.infrastructure.messaging.handle;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;

import java.util.Set;

public interface AgentMessagingCapability {
    /**
     * @return - set of receiver idenitifiers or error
     */
    Result<Set<AID>, FIPAException> send(final ACLMessage aclMessage, final ServiceDescription serviceDescription);

    static AgentMessagingCapability defaultCapability(final ServiceDiscovery discovery, final Agent agent) {
        return new DefaultMessagingCapability(discovery, agent);
    }
}
