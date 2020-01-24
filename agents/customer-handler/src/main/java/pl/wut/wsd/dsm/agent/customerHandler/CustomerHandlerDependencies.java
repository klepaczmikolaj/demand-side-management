package pl.wut.wsd.dsm.agent.customerHandler;

import lombok.Builder;
import lombok.Getter;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerOfferRepository;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

@Getter
@Builder
class CustomerHandlerDependencies {
    private final Customer customer;
    private final CustomerObligationRepository customerObligationRepository;
    private final CustomerOfferRepository customerOfferRepository;
    private final CustomerRepository customerRepository;
    private final Codec codec;
}
