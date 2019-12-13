package pl.wut.wsd.dsm.agent.quote_manager.draft;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.ontology.draft.ObligationType;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ObligationInDraft {
    private final double kwsReduction;
    private final double kwsIncrease;
    private final ObligationType obligationType;


    public static ObligationInDraft increase(final double kws) {
        return new ObligationInDraft(0, kws, ObligationType.INCREASE);
    }

    public static ObligationInDraft decrease(final double kws) {
        return new ObligationInDraft(kws, 0, ObligationType.REDUCTION);
    }


}
