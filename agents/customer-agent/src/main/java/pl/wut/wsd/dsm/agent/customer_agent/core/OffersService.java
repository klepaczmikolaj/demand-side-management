package pl.wut.wsd.dsm.agent.customer_agent.core;

import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;

import java.util.List;
import java.util.Optional;

public interface OffersService {

    Optional<CustomerOfferRepresentation> getCurrentOffer();

    List<CustomerOfferRepresentation> getOfferHistory();

    void registerOffer(CustomerOffer offer);
}
