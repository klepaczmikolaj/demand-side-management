package pl.wut.wsd.dsm.agent.network_advisor.domain.profile;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class DateBaseInterpolator<T extends Number> {

    private final Map<ZonedDateTime, T> measurements;

    protected double getAt(final ZonedDateTime at) {
        final Set<ZonedDateTime> dates = measurements.keySet();
        final List<ZonedDateTime> sortedDates = dates.stream().sorted().collect(Collectors.toList());

        final Dates closestDates = findClosesDates(at, sortedDates);

        final T y1 = measurements.get(closestDates.closestBefore);
        final T y2 = measurements.get(closestDates.closestAfter);

        final double secondDistance = closestDates.closestAfter.getSecond() - closestDates.closestBefore.getSecond();
        final double x1Distance = Duration.between(closestDates.closestBefore, at).getSeconds();
        final double x2Distance = Duration.between(closestDates.closestAfter, at).getSeconds();

        return ((x1Distance * y1.doubleValue()) + x2Distance * y2.doubleValue()) / secondDistance;
    }

    private Dates findClosesDates(final ZonedDateTime at, final List<ZonedDateTime> sortedDates) {
        final ListIterator<ZonedDateTime> iterator = sortedDates.listIterator();
        final ZonedDateTime last = sortedDates.get(sortedDates.size() - 1);
        final ZonedDateTime beforeLast = sortedDates.get(sortedDates.size() - 2);
        if (beforeLast.isBefore(at) && last.isBefore(at)) {
            return Dates.of(beforeLast, last);
        }

        while (iterator.hasNext()) {
            final ZonedDateTime next = iterator.next();
            if (next.isAfter(at)) {
                if (iterator.hasPrevious()) {
                    return Dates.of(iterator.previous(), next);
                } else {
                    return Dates.of(next, iterator.next());
                }
            }
        }
        throw new RuntimeException("xd");
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class Dates {
        private final ZonedDateTime closestBefore;
        private final ZonedDateTime closestAfter;
    }
}
