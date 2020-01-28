package pl.wut.wsd.dsm.agent.external.google;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.wut.wsd.dsm.agent.customer_agent.notification.CustomerNotification;
import pl.wut.wsd.dsm.agent.customer_agent.notification.NotificationAdapter;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

import java.io.IOException;
import java.net.URI;

@Slf4j
public class GoogleNotificationsAdapter implements NotificationAdapter {

    private final URI firebaseUri = URI.create("https://fcm.googleapis.com/fcm/send");
    private final Codec codec = Codec.json();
    final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    private final String notificationKey;
    private String customerNotificationId;

    public GoogleNotificationsAdapter(final String notificationKey) {
        this.notificationKey = notificationKey;
    }


    @Override
    public void sendNotification(final CustomerNotification customerNotification) {
        if (customerNotificationId == null) {
            log.error("Cannot send notification, customer notification id is not set");
            return;
        }
        final Notification notification = new Notification(customerNotification.getTitle(), customerNotification.getCustomerMessage());
        final Message message = new Message(852588322724L, notification);
        final GoogleNotification googleNotification = new GoogleNotification(customerNotificationId, message);

        try {
            final Request request = new Request.Builder()
                    .header("Authorization", "key=" + notificationKey)
                    .header("Accept", "application/json")
                    .header("Accept-Encoding", "gzip, deflate")
                    .url(firebaseUri.toURL())
                    .post(RequestBody.create(codec.encode(googleNotification), MediaType.get("application/json")))
                    .build();
            final Response response = okHttpClient.newCall(request).execute();
            log.info("Sent notification, response status {}", response.code());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setCustomerNotificationId(final String notificationKey) {
        this.customerNotificationId = notificationKey;
    }

}
