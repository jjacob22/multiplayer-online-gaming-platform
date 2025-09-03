package networking.requestMessages;

import profile.Notification;

import java.io.Serializable;

public record DenyFriendRequest(String sender) implements Serializable {
}
