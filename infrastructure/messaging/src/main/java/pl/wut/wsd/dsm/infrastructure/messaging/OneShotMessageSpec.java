package pl.wut.wsd.dsm.infrastructure.messaging;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Consumer;

@Accessors(fluent = true)
@Getter
@RequiredArgsConstructor
public class OneShotMessageSpec implements IMessageSpecification {

    private final MessageTemplate messageTemplate;
    private final Consumer<ACLMessage> action;

    @Override
    public boolean isOneShot() {
        return true;
    }
}
