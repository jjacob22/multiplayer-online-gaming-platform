package networking.responseMessages;

import java.io.Serializable;

/**
 * A record object for a chat message response from the server
 *
 * @param from The username of the user who sent the message.
 * @param message chat message content
 */
public record ChatMessageResponse(String from, String message) implements Serializable,Response {
}
