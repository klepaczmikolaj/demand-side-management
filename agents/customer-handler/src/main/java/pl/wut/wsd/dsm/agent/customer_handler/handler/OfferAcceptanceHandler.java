package pl.wut.wsd.dsm.agent.customer_handler.handler;

import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.customer_handler.model.Obligation;
import pl.wut.wsd.dsm.agent.customer_handler.model.Offer;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customer_handler.persistence.CustomerOfferRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.handle.ParsingHandler;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class OfferAcceptanceHandler extends ParsingHandler<CustomerObligation, CustomerDraftProtocol.AcceptClientDecision> {

    private final CustomerOfferRepository customerOfferRepository;
    private final CustomerObligationRepository obligationRepository;

    public OfferAcceptanceHandler(final Codec codec, final CustomerDraftProtocol.AcceptClientDecision protocolStep, final CustomerOfferRepository customerOfferRepository, final CustomerObligationRepository obligationRepository) {
        super(codec, protocolStep);
        this.customerOfferRepository = customerOfferRepository;
        this.obligationRepository = obligationRepository;
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
        final Obligation obligation = Obligation.newObligation(relatedOffer.getCustomerId(), kws, relatedOffer);
        obligationRepository.saveOrUpdate(obligation);
    }
}
