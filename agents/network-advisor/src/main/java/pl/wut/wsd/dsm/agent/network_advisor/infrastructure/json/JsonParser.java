package pl.wut.wsd.dsm.agent.network_advisor.infrastructure.json;

@FunctionalInterface
public interface JsonParser<T> {
    T parse(String json);
}
