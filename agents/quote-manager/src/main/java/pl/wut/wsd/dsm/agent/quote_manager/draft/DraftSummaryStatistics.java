package pl.wut.wsd.dsm.agent.quote_manager.draft;

import lombok.Builder;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@ToString
public class DraftSummaryStatistics {
    public final UUID draftId;
    public final double totalObligationIncrease;
    public final double totalObligationDecrease;

    public final double totalOfferedIncrease;
    public final double totalOfferedDecrease;

    public final ZonedDateTime since;
    public final ZonedDateTime until;

}
