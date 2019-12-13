package pl.wut.wsd.dsm.agent.customer_agent.rest;

import io.javalin.Javalin;
import io.javalin.http.Context;
import pl.wut.wsd.dsm.agent.customer_agent.CustomerAgentApiHandle;
import pl.wut.wsd.dsm.agent.customer_agent.device.Device;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.ApiError;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.CustomerSettings;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationAcceptanceRequest;
import pl.wut.wsd.dsm.agent.customer_agent.rest.model.obligation.ObligationRepresentation;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;

public class ApiInitializer {

    final Codec codec = Codec.json();

    public void initialize(final Javalin javalin, final CustomerAgentApiHandle customerAgentApiHandle) {
        javalin.post("/obligation", ctx -> {
            final Result<ObligationRepresentation, ApiError> result = parseBody(ObligationAcceptanceRequest.class, ctx.body())
                    .flatMap(customerAgentApiHandle::postObligation);
            respond(result, ctx);
        });

        // Wrzucenie tokenu do wiadomości push
        javalin.post("/pushToken", ctx -> {
            final String token = ctx.body();

        });

        javalin.get("/devices", ctx -> {
            respond(customerAgentApiHandle.getDevices(), ctx);
        });

        javalin.post("/devices/:id/on", ctx -> {
            final Result<Device, ApiError> result = parseBody(Long.class, ctx.pathParam("id"))
                    .flatMap(id -> customerAgentApiHandle.switchDevice(id, true));
            respond(result, ctx);
        });

        javalin.post("/devices/:id/off", ctx -> {
            final Result<Device, ApiError> result = parseBody(Long.class, ctx.pathParam("id"))
                    .flatMap(id -> customerAgentApiHandle.switchDevice(id, false));
            respond(result, ctx);
        });

        javalin.get("/obligations", ctx -> respond(customerAgentApiHandle.getObligationHistory(), ctx));

        javalin.get("/obligations/current", ctx -> respond(customerAgentApiHandle.getCurrentObligation(), ctx));

        javalin.get("/offers/current", ctx -> respond(customerAgentApiHandle.getCurrentOffer(), ctx));

        javalin.get("/offers", ctx -> respond(customerAgentApiHandle.getOffersHistory(), ctx));

        javalin.get("/settings", ctx -> respond(customerAgentApiHandle.getCustomerSettings(), ctx));

        javalin.post("/settings", ctx -> {
            final Result<CustomerSettings, ApiError> result = parseBody(CustomerSettings.class, ctx.body())
                    .flatMap(customerAgentApiHandle::updateCustomerSettings);

            respond(result, ctx);
        });
    }

    private <T> Result<T, ApiError> parseBody(final Class<T> targetClass, final String json) {
        final Result<T, DecodingError> decodingResult = codec.decode(json, targetClass);

        return decodingResult.isValid() ? Result.ok(decodingResult.result()) : Result.error(ApiError.badRequest(decodingResult.error().getMessage()));
    }

    private void respond(final Result<?, ApiError> response, final Context responseContext) {
        if (response.isValid()) {
            responseContext.status(200).result(codec.encode(response.result()));
        } else {
            final ApiError error = response.error();
            responseContext.status(error.getCode()).result(codec.encode(error));
        }
    }
}
