package pl.wut.wsd.dsm.agent.network_advisor.domain.profile;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@FunctionalInterface
public interface ElectricityProductionProfile {
    BigInteger getProductionInWatts(final ZonedDateTime at);
}
