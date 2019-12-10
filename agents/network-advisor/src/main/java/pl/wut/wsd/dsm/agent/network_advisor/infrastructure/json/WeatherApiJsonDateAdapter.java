package pl.wut.wsd.dsm.agent.network_advisor.infrastructure.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * All dates are UTC
 */
public class WeatherApiJsonDateAdapter extends TypeAdapter<ZonedDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(final JsonWriter out, final ZonedDateTime value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public ZonedDateTime read(final JsonReader in) throws IOException {
        return ZonedDateTime.of(LocalDateTime.parse(in.nextString(), formatter), ZoneId.of("UTC"));
    }
}
