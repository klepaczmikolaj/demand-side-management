package pl.wut.wsd.dsm.agent.quote_manager;

import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupInfoImpl;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupManager;

@Slf4j
public class QuoteAgentApplication {

    /* -------------------------- Properties ------------------------ */
    private static final String mainContainerHost = "localhost";
    private static final int mainContainerPort = 1099;

    private static final AgentStartupManager agentStartupManager = new AgentStartupManager();

    public static void main(final String[] args) throws StaleProxyException {
        final AgentContainer agentContainer = createAgentContainer();
        agentStartupManager.startAgent(agentContainer, QuoteAgent.class, "quote-manager", null);
    }

    private static AgentContainer createAgentContainer() {
        return agentStartupManager.startChildContainer(AgentStartupInfoImpl.builder()
                .platformId("wsd-dsm")
                .containerName("quote-container")
                .mainContainerHost(mainContainerHost)
                .mainContainerPort(mainContainerPort)
                .build());
    }
}
