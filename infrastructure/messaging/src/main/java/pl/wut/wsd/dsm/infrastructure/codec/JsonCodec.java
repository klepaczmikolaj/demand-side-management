package pl.wut.wsd.dsm.infrastructure.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.infrastructure.codec.json.LocalDateTimeAdapter;
import pl.wut.wsd.dsm.infrastructure.codec.json.ZonedDateTimeAdapter;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JsonCodec implements Codec {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public <R> Result<R, DecodingError> decode(final String representation, final Class<R> ontology) {
        return Result.asExceptionHandler(representation, r -> gson.fromJson(r, ontology), DecodingError::ofCause);
    }

    @Override
    public <R> String encode(final R object) {
        return gson.toJson(object);
    }

    @Override
    public String language() {
        return "json";
    }
}
