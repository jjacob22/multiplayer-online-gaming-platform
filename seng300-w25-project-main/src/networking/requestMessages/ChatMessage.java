package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for sending/receiving chat messages.
 * @param message the message
 */
public record ChatMessage(String message) implements Serializable {
}
