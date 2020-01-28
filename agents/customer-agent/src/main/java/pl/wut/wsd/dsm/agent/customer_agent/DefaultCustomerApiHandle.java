package pl.wut.wsd.dsm.agent.customer_agent;

import lombok.RequiredArgsConstructor;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customer_agent.core.ObligationsService;
import pl.wut.wsd.dsm.agent.customer_agent.core.OffersService;
import pl.wut.wsd.dsm.agent.customer_agent.device.Device;
import pl.wut.wsd.dsm.agent.customer_agent.device.Devices;
import pl.wut.wsd.dsm.agent.customer_agent.notification.NotificationAdapter;
import pl.wut.wsd.dsm.agent.customer_agent.rest.CustomerAgentApiHandle;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.ApiError;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerOfferRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerSettings;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.agent.customer_agent.settings.SettingsService;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DefaultCustomerApiHandle implements CustomerAgentApiHandle {

    private final ObligationsService obligationsService;
    private final OffersService offersService;
    private final SettingsService settingsService;
    private final Devices devices;
    private final Customer customer;
    private final NotificationAdapter notificationsAdapter;

    @Override
    public Result<ObligationRepresentation, ApiError> postObligation(final ObligationAcceptanceRequest acceptance) {
        return obligationsService.acceptObligation(acceptance);
    }

    @Override
    public Result<List<Device>, ApiError> getDevices() {
        return Result.ok(devices.getDevices());
    }

    @Override
    public Result<List<ObligationRepresentation>, ApiError> getObligationHistory() {
        return Result.ok(obligationsService.getObligationHistory());
    }

    @Override
    public Result<List<CustomerOfferRepresentation>, ApiError> getCurrentOffer() {
        final List<CustomerOfferRepresentation> offers = offersService.getPendingOffers();
        return offers.isEmpty() ? Result.error(ApiError.notFound("No current offer for customer")) : Result.ok(offers);
    }

    @Override
    public Result<List<CustomerOfferRepresentation>, ApiError> getOffersHistory() {
        return Result.ok(offersService.getOfferHistory());
    }

    @Override
    public Result<List<ObligationRepresentation>, ApiError> getCurrentObligation() {
        final List<ObligationRepresentation> obligation = obligationsService.getCurrentObligations();

        return obligation.isEmpty() ? Result.error(ApiError.notFound("No obligation under realization")) : Result.ok(obligation);
    }

    @Override
    public Result<CustomerSettings, ApiError> updateCustomerSettings(final CustomerSettings customerSettings) {
        if (customerSettings.getMinimalProfit() != null && customerSettings.getMinimalProfit() <= 0) {
            return Result.error(ApiError.badRequest("Minimal profit must be greater than 0"));
        }
        if (customerSettings.getNotificationsKey() == null || customerSettings.getNotificationsKey().isEmpty()) {
            return Result.error(ApiError.badRequest("Notification key cannot be null or empty"));
        }
        return Result.ok(settingsService.updateCustomerSettings(customerSettings.getMinimalProfit(), customerSettings.getNotificationsKey()));
    }

    @Override
    public Result<CustomerSettings, ApiError> getCustomerSettings() {
        return Result.ok(settingsService.getSettings());
    }

    @Override
    public Result<Device, ApiError> switchDevice(final Long id, final boolean on) {
        final Optional<Device> deviceOpt = devices.findById(id);
        if (!deviceOpt.isPresent()) {
            return Result.error(ApiError.notFound(String.format("Device of id %d not found", id)));
        } else {
            deviceOpt.get().setOn(on);
            return Result.ok(deviceOpt.get());
        }
    }

    @Override
    public Result<String, ApiError> setPushToken(final String notificationKey) {
        notificationsAdapter.setCustomerNotificationId(notificationKey);

        return Result.ok("Poprawnie ustawiono klucz użytkownika");
    }

    @Override
    public int getCustomerCid() {
        return customer.getCustomerId().intValue();
    }
}
