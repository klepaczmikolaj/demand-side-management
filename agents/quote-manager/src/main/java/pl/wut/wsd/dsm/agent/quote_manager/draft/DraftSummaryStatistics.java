package pl.wut.wsd.dsm.agent.quote_manager.draft;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class DraftSummaryStatistics {
    public final double totalObligationIncrease;
    public final double totalObligationDecrease;

    public final double totalOfferedIncrease;
    public final double totalOfferedDecrease;

}
