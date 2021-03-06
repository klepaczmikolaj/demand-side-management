package pl.wut.wsd.dsm.infrastructure.common.function;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@Accessors(fluent = true)
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<L, R> {

    @Getter
    private final L result;

    @Getter
    private final R error;

    public static <L, R> Result<L, R> ok(final L left) {
        return new Result<>(Objects.requireNonNull(left), null);
    }

    public static <L, R> Result<L, R> error(final R right) {
        return new Result<>(null, Objects.requireNonNull(right));
    }

    public static <T, L, R> Result<L, R> asExceptionHandler(final T input, final Function<T, L> mapping, final BiFunction<Exception, T, R> exceptionMapper) {
        try {
            return Result.ok(mapping.apply(input));
        } catch (final Exception e) {
            return Result.error(exceptionMapper.apply(e, input));
        }
    }

    public L throwingGet(final Function<R, ? extends Exception> mapper) throws Exception {
        if (!isValid()) {
            throw mapper.apply(error);
        }
        return result;
    }

    public L throwingGetRuntime(final Function<R, ? extends RuntimeException> mapper) {
        if (!isValid()) {
            throw mapper.apply(error);
        }
        return result;
    }

    public <X> Result<X, R> mapResult(final Function<L, X> mapper) {
        return this.isValid() ? Result.ok(mapper.apply(this.result)) : Result.error(this.error);
    }

    public <X> Result<X, R> flatMap(final Function<L, Result<X, R>> mapper) {
        return this.isValid() ? mapper.apply(this.result) : Result.error(this.error);
    }

    public boolean isValid() {
        return result != null;
    }

    public boolean isError() {
        return !isValid();
    }
}
