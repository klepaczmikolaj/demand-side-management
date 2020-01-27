package pl.wut.wsd.dsm.agent.customer_agent.core;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.customer_agent.device.Devices;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.handle.AgentMessagingCapability;
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumption;
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumptionProtocol;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CurrentUsageSenderImpl implements CurrentUsageSender {

    private final Devices devices;
    private final EnergyConsumptionProtocol.ConsumptionInformation step;
    private final ServiceDiscovery discovery;
    private final Codec codec;
    private final AgentMessagingCapability messagingCapability;

    @Override
    public void sendToCustomerHandler() {
        final Result<List<DFAgentDescription>, FIPAException> services = discovery.findServices(step.getTargetService());
        if (services.isError()) {
            log.error("Cannot notify customer handler of current usage", devices);
        } else if (services.result().isEmpty()) {
            log.error("Not customer handler to notify of current usage found", devices);
        } else {
            final ACLMessage message = step.templatedMessage();
            final EnergyConsumption energyConsumption = new EnergyConsumption(ZonedDateTime.now(), devices.getCurrentUsageInWatts());
            message.setContent(codec.encode(energyConsumption));
            services.result().stream().map(DFAgentDescription::getName).forEach(message::addReceiver);
            messagingCapability.send(message);
            log.info("current usage information sent");
        }
    }
}
