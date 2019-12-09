package pl.wut.wsd.dsm.agent.gateway;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupInfoImpl;
import pl.wut.wsd.dsm.infrastructure.startup.AgentStartupManager;

import java.net.URL;
import java.util.Optional;

@Slf4j
public class GatewayAgentApplication {


    public static void main(final String[] args) throws StaleProxyException {
        final AgentStartupManager agentStartupManager = new AgentStartupManager();
        final Javalin api = Javalin.create(JavalinConfig::enableCorsForAllOrigins).start(8080);
        final GatewayAgentDelegate delegate = new GatewayAgentDelegate();

        api.get("/", ctx -> {
            final Long customerId = ctx.queryParam("customerId", Long.class).getValue();
            if (customerId == null) {
                ctx.result("Please provide customerId");
                ctx.status(HttpStatus.BAD_REQUEST_400);
                return;
            }

            log.info("All started");
            final Optional<URL> customerApiLocation = delegate.getCustomerApiLocation(customerId);

            if (customerApiLocation.isPresent()) {
                ctx.result(customerApiLocation.get().toString());
            } else {
                ctx.status(HttpStatus.NOT_FOUND_404);
                ctx.result("Api for customer: " + customerId + " not found");
            }
        });
        final AgentContainer container = agentStartupManager.startChildContainer(AgentStartupInfoImpl.builder()
                .platformId("wsd-dsm")
                .containerName("gateway-agent-container")
                .mainContainerHost("localhost")
                .mainContainerPort(1099)
                .build());
        agentStartupManager.startAgent(container, GatewayAgent.class, "gateway-agent", delegate);


    }

}
