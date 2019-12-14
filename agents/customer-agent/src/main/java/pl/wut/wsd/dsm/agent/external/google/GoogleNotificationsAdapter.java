package pl.wut.wsd.dsm.agent.external.google;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.wut.wsd.dsm.agent.customer_agent.notification.CustomerNotification;
import pl.wut.wsd.dsm.agent.customer_agent.notification.NotificationAdapter;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

public class GoogleNotificationsAdapter implements NotificationAdapter {

    private final URI firebaseUri = URI.create("https://fcm.googleapis.com/fcm/send");
    private final Codec codec = Codec.json();
    private final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void sendNotification(final CustomerNotification customerNotification) {
        final Notification notification = new Notification(customerNotification.getTitle(), customerNotification.getCustomerMessage());
        final Message message = new Message(852588322724L, notification);
        final GoogleNotification googleNotification = new GoogleNotification(customerNotification.getToIdentifier(), message);

        try {
            final Request request = new Request.Builder()
                    .url(firebaseUri.toURL())
                    .post(RequestBody.create(codec.encode(googleNotification), MediaType.get("application/json")))
                    .build();
            final Response response = okHttpClient.newCall(request).execute();
            System.out.println(response);
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
