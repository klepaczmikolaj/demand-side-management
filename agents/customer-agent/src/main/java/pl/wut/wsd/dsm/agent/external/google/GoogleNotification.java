package pl.wut.wsd.dsm.agent.external.google;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleNotification {

    @SerializedName("to")
    private final String toIdentifier;
    private final Message message;
}

@RequiredArgsConstructor
class Message {
    private final Long token;
    private final Notification notification;
}

@Getter
@RequiredArgsConstructor
class Notification {
    private final String title;
    private final String body;
}