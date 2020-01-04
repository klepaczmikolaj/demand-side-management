package pl.wut.wsd.dsm.agent.network_advisor.domain.profile;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Map;

public class InterpolatedElectricityDemandProfile extends DateBaseInterpolator<BigInteger> implements ElectricityDemandProfile {


    public InterpolatedElectricityDemandProfile(final Map<ZonedDateTime, BigInteger> measurements) {
        super(measurements);
    }

    @Override
    public BigInteger getDemandInWatts(final ZonedDateTime at) {
        return BigInteger.valueOf(Double.valueOf(super.getAt(at)).longValue());
    }
}
