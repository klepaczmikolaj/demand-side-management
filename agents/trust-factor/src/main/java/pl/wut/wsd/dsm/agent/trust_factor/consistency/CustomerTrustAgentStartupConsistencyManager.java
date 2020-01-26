package pl.wut.wsd.dsm.agent.trust_factor.consistency;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.CustomerLoader;

@RequiredArgsConstructor
public class CustomerTrustAgentStartupConsistencyManager {

    private final CustomerLoader customerLoader;


    public void runOnStartup() {

    }
}
