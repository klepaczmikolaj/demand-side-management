package pl.wut.wsd.dsm.infrastructure.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.infrastructure.function.Result;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JsonCodec implements Codec {

    private static final Gson gson = new GsonBuilder().create();

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
