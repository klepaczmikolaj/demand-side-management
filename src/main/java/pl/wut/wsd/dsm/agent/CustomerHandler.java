package pl.wut.wsd.dsm.agent;

import pl.wut.wsd.dsm.ontology.auction.CustomerObligation;
import pl.wut.wsd.dsm.ontology.auction.CustomerOffer;

public interface CustomerHandler {
    CustomerOffer prepareCustomerOffer();

    void processCustomerObligation(CustomerObligation customerObligation);
}