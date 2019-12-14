package pl.wut.wsd.dsm.agent.customer_agent.notification;

public interface NotificationAdapter {
    void sendNotification(final CustomerNotification notification);
}