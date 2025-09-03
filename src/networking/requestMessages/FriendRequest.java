package networking.requestMessages;

import java.io.Serializable;

/**
 * Message to make a friend request.
 * @param userToFriend The user which we would like to be friends with.
 */
public record FriendRequest(String userToFriend) implements Serializable {
}
