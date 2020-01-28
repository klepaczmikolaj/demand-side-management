package pl.wut.wsd.dsm.agent.customer_agent.core;

import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;

import java.util.List;

public interface OffersService {

    List<CustomerOfferRepresentation> getPendingOffers();

    List<CustomerOfferRepresentation> getOfferHistory();
}
