package pl.wut.wsd.dsm.protocol.consumption;

import jade.domain.FIPAAgentManagement.Property;
import jade.lang.acl.ACLMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.protocol.Protocol;
import pl.wut.wsd.dsm.protocol.TargetedStep;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EnergyConsumptionProtocol extends Protocol {

    private static final EnergyConsumptionProtocol instance = new EnergyConsumptionProtocol();
    private final ServiceDescriptionFactory factory = new ServiceDescriptionFactory();

    public ConsumptionInformation informationStep(final Long customerId) {
        return new ConsumptionInformation(customerId);
    }

    public class ConsumptionInformation extends TargetedStep<EnergyConsumptionProtocol, EnergyConsumption> {
        private final Long customerId;

        private ConsumptionInformation(final Long customerId) {
            super("Inform of consumption", ACLMessage.INFORM, true, EnergyConsumption.class, instance, factory.nameAndProperties("consumption-consumer", new Property("customerId", customerId)));
            this.customerId = customerId;
        }
    }
}