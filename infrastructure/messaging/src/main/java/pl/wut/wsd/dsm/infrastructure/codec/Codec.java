package pl.wut.wsd.dsm.infrastructure.codec;


import pl.wut.wsd.dsm.infrastructure.function.Result;

public interface Codec {


    static Codec json() {
        return new JsonCodec();
    }

    <R> Result<R, DecodingError> decode(String representation, Class<R> ontology);

    <R> String encode(R object);

    String language();
}
