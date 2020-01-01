package pl.wut.wsd.dsm.agent.quote_manager.draft;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
class Draft {
    final Set<ObligationInDraft> obligationsInDraft = new HashSet<>();
    final Set<CustomerOffer> customerOffers = new HashSet<>();
    final ZonedDateTime draftEnd;
    final ZonedDateTime demandChangeSince;
    final ZonedDateTime demandChangeEnd;
}
