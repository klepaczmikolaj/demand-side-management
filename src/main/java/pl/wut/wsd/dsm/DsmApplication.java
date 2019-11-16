package pl.wut.wsd.dsm;

import jade.Boot;
import pl.wut.wsd.dsm.agent.network.NetworkAgent;
import pl.wut.wsd.dsm.agent.quote.QuoteAgent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DsmApplication {

    private static final String agentArgumentsSeparator = ";";

    public static void main(final String[] args) {
        final String agentsArgument = prepareAgentsArgumentString(QuoteAgent.class, NetworkAgent.class);
        Boot.main(new String[]{"-gui", "-agents", agentsArgument});
    }

    private static String prepareAgentsArgumentString(final Class<?>... classes) {
        return Stream.of(classes)
                .map(DsmApplication::toArgumentString)
                .collect(Collectors.joining(agentArgumentsSeparator));
    }

    private static String toArgumentString(final Class<?> agentClazz) {
        return String.format("%s:%s", agentClazz.getSimpleName(), agentClazz.getCanonicalName());
    }
}
