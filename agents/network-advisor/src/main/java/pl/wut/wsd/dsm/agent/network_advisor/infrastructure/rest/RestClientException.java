package pl.wut.wsd.dsm.agent.network_advisor.infrastructure.rest;

import lombok.Getter;

import java.util.Optional;

public class RestClientException extends RuntimeException {
    @Getter
    private final int responseCode;

    private final String errorDetail;

    RestClientException(final int responseCode, final String message) {
        this.responseCode = responseCode;
        this.errorDetail = message;
    }

    public Optional<String> getErrorDetail() {
        return Optional.ofNullable(errorDetail);
    }

}
