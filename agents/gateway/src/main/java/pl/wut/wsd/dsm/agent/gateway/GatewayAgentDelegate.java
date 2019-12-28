package pl.wut.wsd.dsm.agent.gateway;

import java.net.URL;
import java.util.Optional;

class GatewayAgentDelegate {
    GatewayAgent gatewayAgent;

    Optional<URL> getCustomerApiLocation(final Long customerId) {
        return Optional.of(gatewayAgent).flatMap(agent -> agent.findCustomerUrl(customerId));
    }
}
