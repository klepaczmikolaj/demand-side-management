package pl.wut.wsd.dsm.infrastructure.messaging;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MessageHandler extends CyclicBehaviour {

    private final List<IMessageSpecification> specifications;

    public MessageHandler(final Agent agent, final IMessageSpecification... specifications) {
        super(agent);
        this.specifications = new ArrayList<>(Arrays.asList(specifications));
    }

    public void addSpecification(final IMessageSpecification messageSpecification) {
        specifications.add(messageSpecification);
    }

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive();
        if (message != null) {
            log.info("Handler for {} received message {}", myAgent.getClass().getSimpleName(), message);
            final List<IMessageSpecification> matchesSpecs = this.specifications.stream()
                    .filter(s -> s.messageTemplate().match(message))
                    .collect(Collectors.toList());
            // do all actions
            matchesSpecs.forEach(s -> s.action().accept(message));
            // remove one shot specifications from handler
            matchesSpecs.stream()
                    .filter(IMessageSpecification::isOneShot)
                    .forEach(specifications::remove);
        } else {
            block();
        }
    }
}
