package pl.wut.wsd.dsm.agent.quote_manager;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

@Builder
@Getter
@Accessors(fluent = true)
class QuoteAgentDependencies {

    private Codec codec;
}
