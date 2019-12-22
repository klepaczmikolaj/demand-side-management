package pl.wut.wsd.dsm.agent.customer_agent.rest.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiError {
    private final String message;
    private transient final int code;

    public static ApiError notFound(final String message) {
        return new ApiError(message, HttpStatus.NOT_FOUND_404);
    }

    public static ApiError badRequest(final String message) {
        return new ApiError(message, HttpStatus.BAD_REQUEST_400);
    }

    public static ApiError internal(final String message) {
        return new ApiError(message, HttpStatus.INTERNAL_SERVER_ERROR_500);
    }
}
