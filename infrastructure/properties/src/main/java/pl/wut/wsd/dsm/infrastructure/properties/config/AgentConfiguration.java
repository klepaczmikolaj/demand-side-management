package pl.wut.wsd.dsm.infrastructure.properties.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class AgentConfiguration {

    public <R> Optional<R> getProperty(final String key, final Function<String, R> parser) {
        return getProperty(key).map(parser);
    }

    /**
     * @throws MissingConfigEntryException on missing entry
     */
    public <R> R getThrowing(final String key, final Function<String, R> parser) throws MissingConfigEntryException {
        return getProperty(key).map(parser).orElseThrow(() -> new MissingConfigEntryException(key));
    }

    /**
     * @throws MissingConfigEntryException on missing entry
     */
    public String getThrowing(final String key) throws MissingConfigEntryException {
        return getProperty(key).orElseThrow(() -> new MissingConfigEntryException(key));
    }


    public abstract Optional<String> getProperty(String key);

    public AgentConfiguration merge(final AgentConfiguration otherSource, final AgentConfigurationPriority priority) {
        final Map<String, String> properties;
        if (priority == AgentConfigurationPriority.THIS) {
            properties = new HashMap<>(otherSource.toPropertiesMap());
            properties.putAll(this.toPropertiesMap());
        } else {
            properties = new HashMap<>(this.toPropertiesMap());
            properties.putAll(otherSource.toPropertiesMap());
        }

        return new MultisourceConfiguration(properties);
    }

    protected abstract Map<String, String> toPropertiesMap();

    public enum AgentConfigurationPriority {
        THIS, OTHER
    }
}
