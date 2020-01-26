package pl.wut.wsd.dsm.agent.customerHandler.handler;

import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Obligation;
import pl.wut.wsd.dsm.agent.customerHandler.domain.model.Offer;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerOfferRepository;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.handle.ParsingHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.handle.AgentMessagingCapability;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.protocol.TargetedStep;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class OfferAcceptanceHandler extends ParsingHandler<CustomerObligation, CustomerDraftProtocol.AcceptClientDecision> {

    private final CustomerOfferRepository customerOfferRepository;
    private final CustomerObligationRepository obligationRepository;
    private final AgentMessagingCapability messages;
    private final CustomerDraftProtocol.InformOfCustomerHandlerAcceptance informCustomer;
    private final TargetedStep<CustomerDraftProtocol, CustomerObligation> sendClientDecision;
    private final CustomerRepository customerRepository;


    public OfferAcceptanceHandler(final Codec codec, final CustomerDraftProtocol.AcceptClientDecision protocolStep, final CustomerOfferRepository customerOfferRepository, final CustomerObligationRepository obligationRepository, final AgentMessagingCapability messages, final CustomerDraftProtocol.InformOfCustomerHandlerAcceptance informCustomer, final CustomerRepository customerRepository) {
        super(codec, protocolStep);
        this.customerOfferRepository = customerOfferRepository;
        this.obligationRepository = obligationRepository;
        this.messages = messages;
        this.informCustomer = informCustomer;
        this.sendClientDecision = informCustomer.getProtocol().sendClientDecision();
        this.customerRepository = customerRepository;
    }

    @Override
    protected void handle(final CustomerObligation customerObligation) {
        final UUID relatedOfferId = customerObligation.getRelatedOfferId();
        final Optional<Offer> offer = customerOfferRepository.findByOfferId(relatedOfferId);
        if (offer.isPresent()) {
            final Offer relatedOffer = offer.get();
            if (relatedOffer.getValidUntil().isAfter(ZonedDateTime.now())) {
                addObligation(relatedOffer, customerObligation);
            } else {
                log.info("Related offer {} is not valid anymore");
            }
        } else {
            log.info("Related offer {} not found", customerObligation.getRelatedOfferId());
        }
    }

    private void addObligation(final Offer relatedOffer, final CustomerObligation dto) {
        final double kws = dto.getObligationType() == ObligationType.INCREASE ? dto.getKwsChange() : dto.getKwsChange();
        if (relatedOffer.getObligation() != null) {
            log.error("Offer {} already has obligation", relatedOffer.getOfferId());
            return;
        }
        final Long customerId = relatedOffer.getCustomer().getCustomerId();
        final pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer customer = customerRepository.findByCustomerId(customerId).get();

        final Obligation obligation = Obligation.newObligation(customer, kws, relatedOffer);
        obligationRepository.saveOrUpdate(obligation);

        informCustomer(dto, customerId);
        informQuoteManager(dto);
    }

    private void informCustomer(final CustomerObligation dto, final Long customerId) {
        final ServiceDescription serviceDescription = informCustomer.serviceDescription(new Customer(customerId));
        final ACLMessage message = informCustomer.templatedMessage();
        message.setContent(codec.encode(dto));
        messages.send(message, serviceDescription);
    }

    private void informQuoteManager(final CustomerObligation dto) {
        final ServiceDescription quoteManager = sendClientDecision.getTargetService();
        final ACLMessage aclMessage = informCustomer.templatedMessage();
        aclMessage.setContent(codec.encode(dto));
        messages.send(aclMessage, quoteManager);
    }
}
