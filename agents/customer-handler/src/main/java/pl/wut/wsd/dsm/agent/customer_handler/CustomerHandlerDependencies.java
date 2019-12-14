package pl.wut.wsd.dsm.agent.customer_handler;

import lombok.Builder;
import lombok.Getter;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerOfferRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

@Getter
@Builder
class CustomerHandlerDependencies {
    private final Customer customer;
    private final CustomerObligationRepository customerObligationRepository;
    private final CustomerOfferRepository customerOfferRepository;
    private final Codec codec;
}
