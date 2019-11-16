package pl.wut.wsd.dsm.agent.network;

import jade.core.behaviours.TickerBehaviour;
import pl.wut.wsd.dsm.agent.NetworkAdvisor;

import java.time.Duration;

public class NetworkMonitor extends TickerBehaviour {

    private final NetworkAdvisor networkAdvisor;

    public NetworkMonitor(final NetworkAgent networkAdvisor) {
        super(networkAdvisor, Duration.ofMinutes(20).toMillis()); // refresh every 20 minutes
        this.networkAdvisor = networkAdvisor;
    }

    @Override
    protected void onTick() {
        //TODO, monitor network if inbalance is expected inform quote manager
    }
}
