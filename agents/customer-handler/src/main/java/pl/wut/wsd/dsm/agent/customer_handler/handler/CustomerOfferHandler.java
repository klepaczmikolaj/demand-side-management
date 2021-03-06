package pl.wut.wsd.dsm.agent.customer_handler.handler;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_handler.CustomerHandlerAgent;
import pl.wut.wsd.dsm.agent.customer_handler.mapper.CustomerHandlerTypesMapper;
import pl.wut.wsd.dsm.agent.customer_handler.model.Offer;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerOfferRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.handle.ParsingHandler;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

import java.util.List;

@Slf4j
public class CustomerOfferHandler extends ParsingHandler<CustomerOffer, CustomerDraftProtocol.SendCustomerOffer> {

    private final CustomerHandlerAgent customerHandlerAgent;
    private final ServiceDiscovery serviceDiscovery;
    private final CustomerDraftProtocol protocol = protocolStep.getProtocol();
    private final CustomerOfferRepository customerOfferRepository;
    private final CustomerHandlerTypesMapper customerHandlerTypesMapper;

    public CustomerOfferHandler(final Codec codec, final CustomerDraftProtocol.SendCustomerOffer protocolStep, final CustomerHandlerAgent customerHandlerAgent, final ServiceDiscovery serviceDiscovery, final CustomerOfferRepository customerOfferRepository, final CustomerHandlerTypesMapper customerHandlerTypesMapper) {
        super(codec, protocolStep);
        this.customerHandlerAgent = customerHandlerAgent;
        this.serviceDiscovery = serviceDiscovery;
        this.customerOfferRepository = customerOfferRepository;
        this.customerHandlerTypesMapper = customerHandlerTypesMapper;
    }

    @Override
    protected void handle(final CustomerOffer customerOffer) {
        final Customer customer = customerHandlerAgent.getCustomer();
        final Result<List<DFAgentDescription>, FIPAException> dfResponse = serviceDiscovery.findServices(protocol.sendCustomerOffer().serviceDescription(customer));
        if (dfResponse.isValid()) {
            final List<DFAgentDescription> result = dfResponse.result();
            persistOffer(customer, customerOffer);
            if (!result.isEmpty()) {
                sendMessage(customerOffer, result);
            } else {
                log.error("No customer service for customer {} found", customer);
            }
        } else {
            log.error("Error fetching data from yellowapages", dfResponse.error());
        }
    }

    private void persistOffer(final Customer customer, final CustomerOffer customerOffer) {
        final Offer offer = customerHandlerTypesMapper.mapToEntity(customerOffer, customer.getCustomerId());
        customerOfferRepository.saveOffer(offer);
    }

    private void sendMessage(final CustomerOffer customerOffer, final List<DFAgentDescription> result) {
        final DFAgentDescription customerAgent = result.get(0);
        final ACLMessage message = protocol.sendCustomerOffer().templatedMessage();
        message.addReceiver(customerAgent.getName());
        message.setContent(codec.encode(customerOffer));
        customerHandlerAgent.send(message);
    }
}
