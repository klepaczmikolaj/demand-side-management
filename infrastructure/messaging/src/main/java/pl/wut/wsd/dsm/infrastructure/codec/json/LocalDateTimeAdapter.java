package pl.wut.wsd.dsm.infrastructure.codec.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(final JsonWriter out, final LocalDateTime value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public LocalDateTime read(final JsonReader in) throws IOException {
        return LocalDateTime.parse(in.nextString());
    }
}
