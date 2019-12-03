package pl.wut.wsd.dsm.protocol;

import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;
import java.util.stream.Stream;

public class ProtocolStep {

    @Getter
    private final String stepName;

    @Getter
    private final int performative;

    @Getter
    private final boolean required;

    @Getter
    private final String targetService;

    @Getter
    private final Class<?> messageClass;

    @Setter
    private Protocol<?> protocol;

    @Builder
    private ProtocolStep(@NonNull final String stepName,
                         @NonNull final int performative,
                         @NonNull final boolean required,
                         @NonNull final String targetService,
                         @NonNull final Class<?> messageClass) {
        this.stepName = Objects.requireNonNull(stepName);
        this.performative = performative;
        this.required = required;
        this.targetService = Objects.requireNonNull(targetService);
        this.messageClass = messageClass;
    }

    public MessageTemplate toMessageTemplate() {
        return Stream.of(
                MessageTemplate.MatchPerformative(performative),
                MessageTemplate.MatchOntology(messageClass.getCanonicalName()),
                MessageTemplate.MatchProtocol(protocol.getName())
        )
                .reduce(MessageTemplate.MatchAll(), MessageTemplate::and);
    }
}
