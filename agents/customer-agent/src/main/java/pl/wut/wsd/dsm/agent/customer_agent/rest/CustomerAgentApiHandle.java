package pl.wut.wsd.dsm.agent.customer_agent.rest;

import pl.wut.wsd.dsm.agent.customer_agent.device.Device;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.ApiError;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerSettings;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;

import java.util.List;

public interface CustomerAgentApiHandle {

    Result<ObligationRepresentation, ApiError> postObligation(final ObligationAcceptanceRequest acceptance);

    Result<List<Device>, ApiError> getDevices();

    Result<List<ObligationRepresentation>, ApiError> getObligationHistory();

    Result<CustomerOfferRepresentation, ApiError> getCurrentOffer();

    Result<List<CustomerOfferRepresentation>, ApiError> getOffersHistory();

    Result<ObligationRepresentation, ApiError> getCurrentObligation();

    Result<CustomerSettings, ApiError> updateCustomerSettings(CustomerSettings customerSettings);

    Result<CustomerSettings, ApiError> getCustomerSettings();

    Result<Device, ApiError> switchDevice(final Long id, boolean on);

}
