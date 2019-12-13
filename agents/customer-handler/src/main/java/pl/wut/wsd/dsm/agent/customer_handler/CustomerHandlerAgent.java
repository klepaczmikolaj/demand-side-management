package pl.wut.wsd.dsm.agent.customer_handler;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

import java.util.List;

@Slf4j
public class CustomerHandlerAgent extends Agent {

    private Customer customer;
    private Codec codec;
    private CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    private final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);

    @Override
    protected void setup() {
        final CustomerHandlerDependencies dependencies = (CustomerHandlerDependencies) getArguments()[0];
        this.customer = dependencies.getCustomer();
        this.codec = dependencies.getCodec();
        this.addBehaviour(
                new MessageHandler(this,
                        MessageSpecification.of(customerDraftProtocol.sendOfferToHandler().toMessageTemplate(), this::handleCustomerOffer),
                        MessageSpecification.of(customerDraftProtocol.acceptClientDecision().toMessageTemplate(), this::handleAcceptance))
        );
        registerToWhitepages();
    }


    private void handleAcceptance(final ACLMessage aclMessage) {
        log.info("User offer accepted: {}", aclMessage.getContent());
        customerDraftProtocol.acceptClientDecision().serviceDescription(customer);
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

    /**
     * Receives customer offer from quote manager and passes it to customer handler.
     */
    private void handleCustomerOffer(final ACLMessage aclMessage) {
        log.info("Received customer offer: ", aclMessage.getContent());
        final Class<CustomerOffer> messageClass = customerDraftProtocol.sendOfferToHandler().getMessageClass();
        final Result<CustomerOffer, DecodingError> decode = codec.decode(aclMessage.getContent(), messageClass);

        if (decode.isError()) {
            log.error("Error occured: " + decode.error().getMessage());
        } else {
            final CustomerOffer customerOffer = decode.result();
            final Result<List<DFAgentDescription>, FIPAException> dfResponse = serviceDiscovery.findServices(customerDraftProtocol.sendCustomerOffer().serviceDescription(customer));
            if (dfResponse.isValid()) {
                final List<DFAgentDescription> result = dfResponse.result();
                if (!result.isEmpty()) {
                    final DFAgentDescription customerAgent = result.get(0);
                    final ACLMessage message = customerDraftProtocol.sendCustomerOffer().templatedMessage();
                    message.addReceiver(customerAgent.getName());
                    message.setContent(codec.encode(customerOffer));
                    send(message);
                } else {
                    log.error("No customer service for customer {} found", customer);
                }
            } else {
                log.error("Error fetching data from yellowapages", dfResponse.error());
            }
            // forward offer to customer

        }
    }


}
