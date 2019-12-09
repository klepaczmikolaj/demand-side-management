package pl.wut.wsd.dsm.agent.trust_factor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.RankingReader;
import pl.wut.wsd.dsm.agent.trust_factor.ranking.TrustRankingRefresher;

@Getter
@Builder(access = AccessLevel.PACKAGE)
class CustomerTrustAgentDependencies {
    private final TrustRankingRefresher trustRankingRefresher;
    private final RankingReader rankingReader;

}
