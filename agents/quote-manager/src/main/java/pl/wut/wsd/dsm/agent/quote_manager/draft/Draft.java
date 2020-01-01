package pl.wut.wsd.dsm.agent.quote_manager.draft;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
class Draft {
    final UUID id;
    final Set<ObligationInDraft> obligationsInDraft = new HashSet<>();
    final Set<CustomerOffer> customerOffers = new HashSet<>();
    final ZonedDateTime draftEnd;
    final ZonedDateTime demandChangeSince;
    final ZonedDateTime demandChangeEnd;
}
