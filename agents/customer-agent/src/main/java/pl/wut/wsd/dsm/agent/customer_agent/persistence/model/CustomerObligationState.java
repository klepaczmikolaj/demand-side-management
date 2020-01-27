package pl.wut.wsd.dsm.agent.customer_agent.persistence.model;

import java.util.Arrays;
import java.util.List;

public enum CustomerObligationState {
    DURING_EVALUATION,
    KEPT,
    FAILED;

    public static final List<CustomerObligationState> RESOLVED = Arrays.asList(KEPT, FAILED);
}
