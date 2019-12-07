package pl.wut.wsd.dsm.agent.customer_agent;

import io.javalin.Javalin;
import lombok.Builder;
import lombok.Getter;
import pl.wut.dsm.ontology.customer.Customer;

@Getter
@Builder
class CustomerAgentDependencies {
    private final Customer customer;
    private final Javalin javalin;
    private final int javalinPort;
}
