package pl.wut.wsd.dsm.agent.customer_handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

@Getter
@RequiredArgsConstructor
public class CustomerHandlerDependencies {
    private final Customer customer;
    private final CustomerObligationRepository customerObligationRepository;
    private final Codec codec;
}
