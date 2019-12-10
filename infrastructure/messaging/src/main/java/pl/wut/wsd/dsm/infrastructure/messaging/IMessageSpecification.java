package pl.wut.wsd.dsm.infrastructure.messaging;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.function.Consumer;

public interface IMessageSpecification {

    MessageTemplate messageTemplate();

    Consumer<ACLMessage> action();

    boolean isOneShot();
}
