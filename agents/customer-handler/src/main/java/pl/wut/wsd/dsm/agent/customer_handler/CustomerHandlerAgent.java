package pl.wut.wsd.dsm.agent.customer_handler;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_handler.handler.CustomerOfferHandler;
import pl.wut.wsd.dsm.agent.customer_handler.handler.OfferAcceptanceHandler;
import pl.wut.wsd.dsm.agent.customer_handler.mapper.CustomerHandlerTypesMapper;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerOfferRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

@Slf4j
public class CustomerHandlerAgent extends Agent {

    @Getter
    private Customer customer;
    private CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();

    @Override
    protected void setup() {
        final CustomerHandlerDependencies dependencies = (CustomerHandlerDependencies) getArguments()[0];
        this.customer = dependencies.getCustomer();
        final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);
        final CustomerObligationRepository obligationRepo = dependencies.getCustomerObligationRepository();
        final CustomerOfferRepository offerRepo = dependencies.getCustomerOfferRepository();
        final Codec codec = dependencies.getCodec();
        final CustomerHandlerTypesMapper mapper = new CustomerHandlerTypesMapper();

        final CustomerOfferHandler customerOfferHandler = new CustomerOfferHandler(codec, new CustomerDraftProtocol().sendCustomerOffer(), this, serviceDiscovery, offerRepo, mapper);
        final OfferAcceptanceHandler offerAcceptanceHandler = new OfferAcceptanceHandler(codec, customerDraftProtocol.acceptClientDecision(), offerRepo, obligationRepo);

        this.addBehaviour(
                new MessageHandler(this,
                        MessageSpecification.of(customerDraftProtocol.sendOfferToHandler().toMessageTemplate(), customerOfferHandler::handle),
                        MessageSpecification.of(customerDraftProtocol.acceptClientDecision().toMessageTemplate(), offerAcceptanceHandler::handle))
        );
        registerToWhitepages();
    }

    private void registerToWhitepages() {
        final DFAgentDescription description = new DFAgentDescription();
        description.setName(this.getAID());
        description.addServices(customerDraftProtocol.sendOfferToHandler().serviceDescription(customer));
        description.addServices(customerDraftProtocol.acceptClientDecision().serviceDescription(customer));

        try {
            DFService.register(this, description);
        } catch (final FIPAException e) {
            log.error("Could not connect to whitepages", e);
        }
    }


}
