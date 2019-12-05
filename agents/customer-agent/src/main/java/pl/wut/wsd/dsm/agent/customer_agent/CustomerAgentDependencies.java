package pl.wut.wsd.dsm.agent.customer_agent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wut.dsm.ontology.customer.Customer;

@Getter
@RequiredArgsConstructor
class CustomerAgentDependencies {
    private final Customer customer;
}
