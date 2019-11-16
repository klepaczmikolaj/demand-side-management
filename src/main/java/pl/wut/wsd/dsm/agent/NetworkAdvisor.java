package pl.wut.wsd.dsm.agent;

import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;

public interface NetworkAdvisor {
    void inform(ExpectedInbalancement expectedInbalancement);
}
