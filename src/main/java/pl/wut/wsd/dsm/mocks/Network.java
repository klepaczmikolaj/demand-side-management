package pl.wut.wsd.dsm.mocks;

import java.time.Instant;

public interface Network {
    DemandAndProduction getEstimatedAt(Instant time);
}
