package pl.wut.wsd.dsm.agent.customer_agent.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomerNotification {
    private final String toIdentifier;
    private String title;
    private final String customerMessage;
}
