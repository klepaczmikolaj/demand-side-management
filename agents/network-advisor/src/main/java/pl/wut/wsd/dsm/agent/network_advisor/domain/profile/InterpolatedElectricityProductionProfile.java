package pl.wut.wsd.dsm.agent.network_advisor.domain.profile;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Map;

public class InterpolatedElectricityProductionProfile extends DateBaseInterpolator<BigInteger> implements ElectricityProductionProfile {

    public InterpolatedElectricityProductionProfile(final Map<ZonedDateTime, BigInteger> measurements) {
        super(measurements);
    }

    @Override
    public BigInteger getProductionInWatts(final ZonedDateTime at) {
        return BigInteger.valueOf((long) getAt(at));
    }
//        final Set<ZonedDateTime> dates = productionPoints.keySet();
//        final List<ZonedDateTime> sortedDates = dates.stream().sorted().collect(Collectors.toList());
//
//        final Dates closestDates = findClosesDates(at, sortedDates);
//
//        final BigInteger y1 = productionPoints.get(closestDates.closestBefore);
//        final BigInteger y2 = productionPoints.get(closestDates.closestAfter);
//
//        final double secondDistance = closestDates.closestAfter.getSecond() - closestDates.closestBefore.getSecond();
//        final double x1Distance = Duration.between(closestDates.closestBefore, at).getSeconds();
//        final double x2Distance = Duration.between(closestDates.closestAfter, at).getSeconds();
//
//        return BigInteger.valueOf((long) ((x1Distance * y1.doubleValue() + x2Distance * y2.doubleValue()) / secondDistance));
//    }
//
//    private Dates findClosesDates(final ZonedDateTime at, final List<ZonedDateTime> sortedDates) {
//        final ListIterator<ZonedDateTime> iterator = sortedDates.listIterator();
//        final ZonedDateTime last = sortedDates.get(sortedDates.size() - 1);
//        final ZonedDateTime beforeLast = sortedDates.get(sortedDates.size() - 2);
//        if (beforeLast.isBefore(at) && last.isBefore(at)) {
//            return Dates.of(beforeLast, last);
//        }

//        while (iterator.hasNext()) {
//            final ZonedDateTime next = iterator.next();
//            if (next.isAfter(at)) {
//                if (iterator.hasPrevious()) {
//                    return Dates.of(iterator.previous(), next);
//                } else {
//                    return Dates.of(next, iterator.next());
//                }
//            }
//        }
//        throw new RuntimeException("xd");
//    }

//    @RequiredArgsConstructor(staticName = "of")
//    private static class Dates {
//        private final ZonedDateTime closestBefore;
//        private final ZonedDateTime closestAfter;
//    }
}

