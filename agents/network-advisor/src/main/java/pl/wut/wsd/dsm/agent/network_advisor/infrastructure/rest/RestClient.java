package pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest;


import java.util.Map;

public interface RestClient {
    <R> R get(String uri, Class<R> clazz, Map<String, String> requestParams);

    static RestClient defaultClient() {
        return new OkHttpBasedRestClientImpl();
    }
}
