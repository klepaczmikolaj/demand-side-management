package pl.wut.wsd.dsm.infrastructure.messaging;

import jade.lang.acl.MessageTemplate;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "beginWith")
public class MessageTemplateJoiner {
    private MessageTemplate messageTemplate;

    public MessageTemplateJoiner and(final MessageTemplate other) {
        this.messageTemplate = MessageTemplate.and(messageTemplate, other);
        return this;
    }

    public MessageTemplateJoiner or(final MessageTemplate other) {
        this.messageTemplate = MessageTemplate.or(messageTemplate, other);
        return this;
    }

    public MessageTemplate get() {
        return messageTemplate;
    }

}
