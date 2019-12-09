package pl.wut.wsd.dsm.agent.customer_agent.rest;

import io.javalin.Javalin;
import pl.wut.wsd.dsm.agent.customer_agent.CustomerAgentApiHandle;

public class ApiInitializer {

    public void initialize(final Javalin javalin, final CustomerAgentApiHandle customerAgentApiHandle) {
//        akceptacja zobowiązania
        javalin.post("/obligation", ctx -> {
            final Long offerId = Long.parseLong(ctx.queryParam("offerId"));

        });
        // Wrzucenie tokenu do wiadomości push
        javalin.post("/pushToken", ctx -> {
            final String token = ctx.body();

        });

        javalin.get("/devices", ctx -> {
//            ctx.pathP
        });

        // włącz wyłącz device
//        java.get
        //historia zobowiązań

        //aktualna oferta

        //aktualnie realizowane zobowiązań

        //ustawienia minimalnego zysku


    }
}
