package pl.wut.wsd.dsm.infrastructure.codec;


import pl.wut.wsd.dsm.infrastructure.function.Result;

public interface Codec {
    <R> Result<R, DecodingError> decode(String representation, Class<R> ontology);

    <R> String encode(R object);
}
