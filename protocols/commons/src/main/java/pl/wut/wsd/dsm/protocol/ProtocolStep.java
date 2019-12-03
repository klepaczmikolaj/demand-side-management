package pl.wut.wsd.dsm.protocol;

import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;
import java.util.stream.Stream;

public class ProtocolStep<T extends Protocol> {

    @Getter
    private final String stepName;

    @Getter
    private final int performative;

    @Getter
    private final boolean required;

    @Getter
    private final ServiceDescription targetService;

    @Getter
    private final Class<?> messageClass;

    private final T protocol;

    @Builder
    private ProtocolStep(@NonNull final String stepName,
                         @NonNull final int performative,
                         @NonNull final boolean required,
                         @NonNull final ServiceDescription targetService,
                         @NonNull final Class<?> messageClass,
                         @NonNull final T protocol) {
        this.stepName = Objects.requireNonNull(stepName);
        this.performative = performative;
        this.required = required;
        this.targetService = Objects.requireNonNull(targetService);
        this.messageClass = messageClass;
        this.protocol = protocol;
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
