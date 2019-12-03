package pl.wut.wsd.dsm.protocol;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;

@Getter
public class Protocol<T> {

    private final String name;
    private final List<ProtocolStep> steps;
    private final T details;

    @Builder
    public Protocol(@NonNull final String name,
                    @NonNull @Singular final List<ProtocolStep> steps,
                    final T details) {
        this.name = name;
        this.steps = steps;
        steps.forEach(protocolStep -> protocolStep.setProtocol(this));
        this.details = details;
    }
}
