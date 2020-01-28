package pl.wut.wsd.dsm.agent.customer_agent.rest;

import io.javalin.Javalin;
import io.javalin.http.Context;
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
        final int cid = customerAgentApiHandle.getCustomerCid();

        javalin.get(prependCid(cid, "/"), ctx -> ctx.result("Hello, customer agent speaking, how can I help you bro"));

        javalin.post(prependCid(cid, "/obligation"), ctx -> {
            final Result<ObligationRepresentation, ApiError> result = parseBody(ObligationAcceptanceRequest.class, ctx.body())
                    .flatMap(customerAgentApiHandle::postObligation);
            respond(result, ctx);
        });

        // Wrzucenie tokenu do wiadomości push
        javalin.post(prependCid(cid, "/pushToken"), ctx -> {
            final String token = ctx.body();
            respond(customerAgentApiHandle.setPushToken(token), ctx);
        });

        javalin.get(prependCid(cid, "/devices"), ctx -> respond(customerAgentApiHandle.getDevices(), ctx));

        javalin.post(prependCid(cid, "/devices/:id/on"), ctx -> {
            final Result<Device, ApiError> result = parseBody(Long.class, ctx.pathParam("id"))
                    .flatMap(id -> customerAgentApiHandle.switchDevice(id, true));
            respond(result, ctx);
        });

        javalin.post(prependCid(cid, "/devices/:id/off"), ctx -> {
            final Result<Device, ApiError> result = parseBody(Long.class, ctx.pathParam("id"))
                    .flatMap(id -> customerAgentApiHandle.switchDevice(id, false));
            respond(result, ctx);
        });

        javalin.get(prependCid(cid, "/obligations"), ctx -> respond(customerAgentApiHandle.getObligationHistory(), ctx));

        javalin.get(prependCid(cid, "/obligations/current"), ctx -> respond(customerAgentApiHandle.getCurrentObligation(), ctx));

        javalin.get(prependCid(cid, "/offers/current"), ctx -> respond(customerAgentApiHandle.getCurrentOffer(), ctx));

        javalin.get(prependCid(cid, "/offers"), ctx -> respond(customerAgentApiHandle.getOffersHistory(), ctx));

        javalin.get(prependCid(cid, "/settings"), ctx -> respond(customerAgentApiHandle.getCustomerSettings(), ctx));

        javalin.post(prependCid(cid, "/settings"), ctx -> {
            final Result<CustomerSettings, ApiError> result = parseBody(CustomerSettings.class, ctx.body())
                    .flatMap(customerAgentApiHandle::updateCustomerSettings);

            respond(result, ctx);
        });
    }

    private String prependCid(final int cid, final String path) {
        return "/" + cid + path;
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
