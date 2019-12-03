package pl.wut.wsd.dsm.infrastructure.messaging;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class MessageHandler extends CyclicBehaviour {

    private final List<MessageSpecification> specifications;

    public MessageHandler(final Agent agent, final MessageSpecification... specifications) {
        super(agent);
        this.specifications = Arrays.asList(specifications);
    }

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive();
        if (message != null) {
            specifications.stream()
                    .filter(s -> s.getTemplateToMatch().match(message))
                    .map(MessageSpecification::getAction)
                    .forEach(action -> action.accept(message));
        } else {
            block();
        }
    }
}
