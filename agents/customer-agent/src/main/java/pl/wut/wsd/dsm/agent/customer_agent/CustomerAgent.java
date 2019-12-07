package pl.wut.wsd.dsm.agent.customer_agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAException;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.infrastructure.InetUtils;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

@Slf4j
public class CustomerAgent extends Agent {

    private CustomerAgentDependencies dependencies;
    private final ServiceDescriptionFactory serviceDescriptionFactory = new ServiceDescriptionFactory();
    private final static String customerApiAddressProperty = "customerApiAddress";

    @Override
    protected void setup() {
        dependencies = (CustomerAgentDependencies) getArguments()[0];
        registerToWhitepages(dependencies.getCustomer().getCustomerId());
        dependencies.getJavalin().get("/", ctx -> ctx.result("Hello, agent " + getAID().getName() + " speaking"));
    }

    @Override
    public void clean(final boolean ok) {
        super.clean(ok);
        try {
            DFService.deregister(this);
            dependencies.getJavalin().stop();
            System.exit(0);
        } catch (final FIPAException e) {
            dependencies.getJavalin().stop();
            System.exit(0);
            log.error("Could not deregister from whitepages");
        }
    }

    private void registerToWhitepages(final Long customerId) {
        final DFAgentDescription dfAgentDescription = new DFAgentDescription();
        final Property cusId = new Property("customerId", customerId);
        final Property restApiAddress = new Property(customerApiAddressProperty, String.format("http://%s:%d/", InetUtils.getMyHostname().orElse("localhost"), dependencies.getJavalinPort()));

        dfAgentDescription.addServices(serviceDescriptionFactory.nameAndProperties("customer-agent", cusId, restApiAddress));
        try {
            DFService.register(this, dfAgentDescription);
        } catch (final FIPAException e) {
            log.error("Could not register to whitepages", e);
        }
    }
}
