package pl.wut.wsd.dsm.agent.customer_handler.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.agent.customer_handler.CustomerHandlerAgent;

@Slf4j
public class MessageDispatcher extends CyclicBehaviour {

    public MessageDispatcher(final CustomerHandlerAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        final ACLMessage received = myAgent.receive();
        if (received == null) {
            block();
        } else {
            log.info("Received message: {}", received);
        }
    }
}
