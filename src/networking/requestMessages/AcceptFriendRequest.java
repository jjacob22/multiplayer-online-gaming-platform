package networking.requestMessages;

import profile.Notification;

import java.io.Serializable;

public record AcceptFriendRequest(String sender) implements Serializable {
}
