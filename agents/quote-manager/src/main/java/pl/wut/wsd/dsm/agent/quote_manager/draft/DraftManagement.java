package pl.wut.wsd.dsm.agent.quote_manager.draft;

import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.infrastructure.common.function.CollectionTransformer;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class DraftManagement {

    private Draft currentDraft;
    private List<Draft> oldDrafts = new ArrayList<>();

    public void startNewDraft(final ZonedDateTime since, final ZonedDateTime to) {
        Optional.ofNullable(currentDraft).ifPresent(oldDrafts::add);
        currentDraft = new Draft(since, to);
    }

    public void registerClientOffers(final Set<CustomerOffer> customerOffer) {
        ensureCurrentDraftInitialized();
        currentDraft.customerOffers.addAll(customerOffer);
    }

    private void ensureCurrentDraftInitialized() {
        if (currentDraft == null)
            throw new IllegalStateException("Current draft should never be null! Please initialize draft before using this method");
    }

    /**
     * @return true if obligation was registered properly, false otherwise
     */
    public boolean registerCustomerObligation(final CustomerObligation customerObligation) {
        ensureCurrentDraftInitialized();
        final Optional<CustomerOffer> relatedOffer = currentDraft.customerOffers.stream()
                .filter(o -> o.getOfferId().equals(customerObligation.getRelatedOfferId()))
                .findFirst();

        if (relatedOffer.isPresent()) {
            final ObligationInDraft obligationInDraft = customerObligation.getObligationType() == ObligationType.INCREASE ?
                    ObligationInDraft.increase(customerObligation.getKwsIncrease()) : ObligationInDraft.decrease(customerObligation.getKwsDecrease());
            currentDraft.obligationsInDraft.add(obligationInDraft);

            return true;
        } else {
            log.info("Offer of id {} not found in current draft, related draft is not active", customerObligation);
            return false;
        }
    }

    public DraftSummaryStatistics getSummaryStatistics() {
        ensureCurrentDraftInitialized();
        return DraftSummaryStatistics.builder()
                .totalObligationDecrease(CollectionTransformer.summing(currentDraft.obligationsInDraft, ObligationInDraft::getKwsReduction))
                .totalObligationIncrease(CollectionTransformer.summing(currentDraft.obligationsInDraft, ObligationInDraft::getKwsIncrease))
                .build();
    }

}
