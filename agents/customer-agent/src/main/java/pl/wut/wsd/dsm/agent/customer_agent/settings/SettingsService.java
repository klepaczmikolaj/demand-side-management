package pl.wut.wsd.dsm.agent.customer_agent.settings;

import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerSettings;

public class SettingsService {

    private CustomerSettings customerSettings = new CustomerSettings(null, null);

    public CustomerSettings getSettings() {
        return customerSettings;
    }

    public CustomerSettings updateCustomerSettings(final Double minimalProfit, final String notificationsKey) {
        customerSettings.setMinimalProfit(minimalProfit);
        customerSettings.setNotificationsKey(notificationsKey);
        return customerSettings;
    }

}
