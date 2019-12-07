package pl.wut.wsd.dsm.infrastructure.properties.config;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

@RequiredArgsConstructor
public class FileConfiguration implements AgentConfiguration {

    private final Properties properties;

    public static Result<FileConfiguration, Exception> fromPath(final Path path) {
        final File file = path.toFile();
        if (!file.exists()) {
            return Result.error(new FileNotFoundException(String.format("File %s does not exist", file.getAbsolutePath())));
        }
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            return Result.ok(new FileConfiguration(properties));
        } catch (final IOException e) {
            return Result.error(e);
        }

    }

    @Override
    public <R> Optional<R> getProperty(final String key, final Function<String, R> parser) {
        return Optional.ofNullable(properties.getProperty(key)).map(parser);
    }

}
