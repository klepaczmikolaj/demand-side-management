package pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

@Slf4j
class OkHttpBasedRestClientImpl implements RestClient {
    private final OkHttpClient client;
    private final Gson gson;
    private static final int OK = 200;
    private static final int SERVER_ERROR = 500;

    public OkHttpBasedRestClientImpl() {
        client = new OkHttpClient();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public <R> R get(final String url, final Class<R> clazz, final Map<String, String> requestParams) {
        final HttpUrl.Builder builder = HttpUrl.get(url).newBuilder();
        if (requestParams != null) {
            requestParams.forEach(builder::addQueryParameter);
        }
        return operation(builder.build(), b -> b.get().build(), json -> gson.fromJson(json, clazz));
    }


    private <R> R operation(final HttpUrl httpUrl, final Function<Request.Builder, Request> toRequest, final Function<String, R> parseResponse) {
        final Request request = toRequest.apply(rqBuilder(httpUrl));
        log.trace("Performing operation url: {} method: {}", httpUrl, request.method());
        try (final Response response = client.newCall(request).execute()) {
            if (OK == response.code())
                return parseResponse.apply(response.body().string());
            else {
                throw new RestClientException(response.code(), response.body().string());
            }
        } catch (final IOException e) {
            throw new RestClientException(SERVER_ERROR, e.getMessage());
        }
    }

    private Request.Builder rqBuilder(final HttpUrl httpUrl) {
        return new Request.Builder().url(httpUrl);
    }

}

