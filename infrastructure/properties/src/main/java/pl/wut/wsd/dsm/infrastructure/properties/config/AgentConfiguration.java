package pl.wut.wsd.dsm.infrastructure.properties.config;

import java.util.Optional;
import java.util.function.Function;

public interface AgentConfiguration {
    <R> Optional<R> getProperty(final String key, final Function<String, R> parser);
}
