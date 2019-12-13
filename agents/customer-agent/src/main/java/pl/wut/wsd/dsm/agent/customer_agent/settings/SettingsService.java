package pl.wut.wsd.dsm.agent.customer_agent.settings;

import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerSettings;

public class SettingsService {

    private CustomerSettings customerSettings = new CustomerSettings(10);

    public CustomerSettings getSettings() {
        return customerSettings;
    }

    public CustomerSettings setMinimalProfil(final double minimalProfit) {
        customerSettings.setMinimalProfit(minimalProfit);
        return customerSettings;
    }

}
