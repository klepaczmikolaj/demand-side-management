package pl.wut.wsd.dsm.infrastructure.properties.config;

public class MissingConfigEntryException extends RuntimeException {

    public MissingConfigEntryException(final String argumentName, final String format) {
        super(String.format("Missing config entry %s of %s format", argumentName, format));
    }
}
