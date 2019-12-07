package pl.wut.wsd.dsm.agent.gateway;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

import java.net.URL;
import java.util.Optional;

@Slf4j
public class GatewayAgentApplication {


    public static void main(final String[] args) throws StaleProxyException {
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

        final AgentContainer mainContainer = createAgentContainer();
        final AgentController agentController = mainContainer.createNewAgent("gateway-agent", GatewayAgent.class.getCanonicalName(), new Object[]{delegate});
        agentController.start();


    }


    private static AgentContainer createAgentContainer() {
        final Runtime runtime = Runtime.instance();
        final Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "wsd-dsm");
        profile.setParameter(Profile.CONTAINER_NAME, "gateway-agent-container");
        profile.setParameter("gui", Boolean.toString(true));
        return runtime.createAgentContainer(profile);
    }
}
