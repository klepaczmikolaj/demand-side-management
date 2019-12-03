package pl.wut.wsd.dsm.agent.customer_handler;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;

@Slf4j
public class CustomerHandlerAgent extends Agent {

    private Customer customer;

    @Override
    protected void setup() {
        final CustomerHandlerDependencies dependencies = (CustomerHandlerDependencies) getArguments()[0];
        this.customer = dependencies.getCustomer();
        registerToWhitepages();

//        this.addBehaviour(new MessageHandler(this, ));
    }

    private void registerToWhitepages() {
        final DFAgentDescription description = new DFAgentDescription();
        description.setName(this.getAID());
        final ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName("customer-handler");
        serviceDescription.addProperties(new Property("customerId", customer.getCustomerId()));
        description.addServices(serviceDescription);

        try {
            DFService.register(this, description);
        } catch (final FIPAException e) {
            log.error("Could not connect");
        }
    }


}
