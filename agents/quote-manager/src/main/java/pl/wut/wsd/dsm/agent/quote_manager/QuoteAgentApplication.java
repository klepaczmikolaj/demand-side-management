package pl.wut.wsd.dsm.agent.quote_manager;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuoteAgentApplication {

    /* -------------------------- Properties ------------------------ */
    private static final String mainContainerHost = "localhost";
    private static final int mainContainerPort = 1099;

    public static void main(final String[] args) throws StaleProxyException {
        final AgentContainer agentContainer = createAgentContainer();

        log.info("Starting quote agent application");

        final AgentController agentController = agentContainer.createNewAgent("quote-manager", QuoteAgent.class.getCanonicalName(), new Object[]{});

        agentController.start();
    }

    private static AgentContainer createAgentContainer() {
        final Runtime runtime = Runtime.instance();
        final Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "wsd-dsm");
        profile.setParameter(Profile.CONTAINER_NAME, "quote-manager-container");
        profile.setParameter(Profile.MAIN_HOST, mainContainerHost);
        profile.setParameter(Profile.MAIN_PORT, Integer.toString(mainContainerPort));

        return runtime.createAgentContainer(profile);
    }
}
