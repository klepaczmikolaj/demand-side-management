package pl.wut.wsd.dsm.agent.customerHandler.domain.model;

import java.util.Arrays;
import java.util.List;

public enum CustomerObligationState {
    DURING_EVALUATION,
    KEPT,
    FAILED;

    public static final List<CustomerObligationState> RESOLVED = Arrays.asList(KEPT, FAILED);
}
