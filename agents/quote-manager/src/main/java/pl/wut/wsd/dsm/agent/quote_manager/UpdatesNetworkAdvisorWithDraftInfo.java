package pl.wut.wsd.dsm.agent.quote_manager;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.quote_manager.draft.DraftManagement;
import pl.wut.wsd.dsm.agent.quote_manager.draft.DraftSummaryStatistics;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.handle.AgentMessagingCapability;
import pl.wut.wsd.dsm.ontology.network.DraftSummary;
import pl.wut.wsd.dsm.protocol.SystemDraftProtocol;
import pl.wut.wsd.dsm.protocol.TargetedStep;

import java.time.Duration;

@Slf4j
public class UpdatesNetworkAdvisorWithDraftInfo extends TickerBehaviour {

    private final Codec codec;
    private final DraftManagement draftManagement;
    private final AgentMessagingCapability messagingCapability;
    private final TargetedStep<SystemDraftProtocol, DraftSummary> step = new SystemDraftProtocol().updateWithDraftSummary();

    public UpdatesNetworkAdvisorWithDraftInfo(final Codec codec, final DraftManagement draftManagement, final Agent a, final ServiceDiscovery serviceDiscovery) {
        super(a, Duration.ofSeconds(2).toMillis());
        this.codec = codec;
        this.draftManagement = draftManagement;
        this.messagingCapability = AgentMessagingCapability.defaultCapability(serviceDiscovery, a);
    }

    @Override
    protected void onTick() {
        if (!draftManagement.currentDraftStillInProgress()) {
            log.info("Not current draft found, no need to update network advisor");
            return;
        }
        final ServiceDescription targetService = step.getTargetService();
        final ACLMessage message = step.templatedMessage();
        message.setContent(codec.encode(prepareSumary()));

        messagingCapability.send(message, targetService);
    }

    private DraftSummary prepareSumary() {
        final DraftSummary summary = new DraftSummary();
        final DraftSummaryStatistics summaryStatistics = draftManagement.getSummaryStatistics();
        summary.setId(summaryStatistics.draftId);
        summary.setIncreaseObligationsSum(summaryStatistics.totalObligationIncrease);
        summary.setReductionObligationsSum(summaryStatistics.totalObligationDecrease);
        summary.setSince(summaryStatistics.since);
        summary.setUntil(summaryStatistics.until);

        return summary;
    }
}
