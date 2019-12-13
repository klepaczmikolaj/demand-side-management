package pl.wut.wsd.dsm.agent.customer_agent.core;

import pl.wut.wsd.dsm.agent.customer_agent.rest.model.ApiError;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;

import java.util.List;
import java.util.Optional;

public interface ObligationsService {

    Optional<ObligationRepresentation> getCurrentObligation();

    List<ObligationRepresentation> getObligationHistory();

    Result<ObligationRepresentation, ApiError> acceptObligation(final ObligationAcceptanceRequest request);
}
