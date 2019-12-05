package pl.wut.wsd.dsm.agent.customer_agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAException;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

@Slf4j
public class CustomerAgent extends Agent {

    private CustomerAgentDependencies dependencies;
    private final ServiceDescriptionFactory serviceDescriptionFactory = new ServiceDescriptionFactory();

    @Override
    protected void setup() {
        dependencies = (CustomerAgentDependencies) getArguments()[0];
        registerToWhitepages(dependencies.getCustomer().getCustomerId());
    }

    private void registerToWhitepages(final Long customerId) {
        final DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescriptionFactory.nameAndProperties("customer-agent", new Property("customerId", customerId)));
        try {
            DFService.register(this, dfAgentDescription);
        } catch (final FIPAException e) {
            log.error("Could not register to whitepages", e);
        }
    }
}
