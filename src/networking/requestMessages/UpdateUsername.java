package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for updating the username
 *
 * @param oldUsername The user's old username
 * @param newUsername The user's new Username
 */
public record UpdateUsername(String oldUsername, String newUsername) implements Serializable {
}
