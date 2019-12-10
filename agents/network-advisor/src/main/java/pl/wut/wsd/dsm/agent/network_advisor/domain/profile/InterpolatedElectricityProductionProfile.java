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
    public BigInteger getProductionInWatts(ZonedDateTime at) {
        Set<ZonedDateTime> dates = productionPoints.keySet();
        List<ZonedDateTime> sortedDates = dates.stream().sorted().collect(Collectors.toList());
        ListIterator<ZonedDateTime> iterator = sortedDates.listIterator();
        
        while(iterator.hasNext() && )
        for (int i = 0; i < sortedDates.size(); i++) {
            ZonedDateTime date = sortedDates.get(i);
            if
        }
        return null;
    }
}
