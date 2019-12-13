package pl.wut.wsd.dsm.agent.network_advisor.domain.profile;

import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InterpolatedElectricityProductionProfile implements ElectricityProductionProfile {

    private final Map<ZonedDateTime, BigInteger> productionPoints;

    @Override
    public BigInteger getProductionInWatts(final ZonedDateTime at) {
        final Set<ZonedDateTime> dates = productionPoints.keySet();
        final List<ZonedDateTime> sortedDates = dates.stream().sorted().collect(Collectors.toList());

        final Dates closestDates = findClosesDates(at, sortedDates);

        return null;
    }

    private Dates findClosesDates(final ZonedDateTime at, final List<ZonedDateTime> sortedDates) {
        final ListIterator<ZonedDateTime> iterator = sortedDates.listIterator();

        final ZonedDateTime next = iterator.next();
        while (iterator.hasNext()) {
            if (next.isAfter(at)) {
                if (iterator.hasPrevious()) {
                    return Dates.of(iterator.previous(), next);
                } else {
                    return Dates.of(next, iterator.next());
                }
            }
        }
        return null; //TODO, ostatnia i przedostatnia
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class Dates {
        private final ZonedDateTime closestBefore;
        private final ZonedDateTime closestAfter;
    }
}

