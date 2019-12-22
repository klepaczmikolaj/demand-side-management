package pl.wut.wsd.dsm.infrastructure.common.function;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionTransformer {

    public static <T, R> List<R> mapToList(final Collection<T> coll, final Function<T, R> mapper) {
        return nullSafeStream(coll).map(mapper).collect(Collectors.toList());
    }

    public static <T> double summing(final Collection<T> collection, final ToDoubleFunction<T> doubleFunction) {
        return nullSafeStream(collection).mapToDouble(doubleFunction).filter(Objects::nonNull).sum();
    }

    public static <T> Optional<T> lastElement(final List<T> list) {
        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(list.size() - 1));
    }

    private static <T> Stream<T> nullSafeStream(final Collection<T> collection) {
        return collection != null ? collection.stream() : Stream.empty();
    }

}
