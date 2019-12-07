package pl.wut.wsd.dsm.infrastructure.properties.config;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
class MultisourceConfiguration extends AgentConfiguration {

    private final Map<String, String> properties;

    @Override
    public <R> Optional<R> getProperty(final String key, final Function<String, R> parser) {
        return Optional.ofNullable(properties.get(key)).map(parser);
    }

    @Override
    public Optional<String> getProperty(final String key) {
        return Optional.ofNullable(properties.get(key));
    }

    @Override
    protected Map<String, String> toPropertiesMap() {
        return new HashMap<>(properties);
    }
}
