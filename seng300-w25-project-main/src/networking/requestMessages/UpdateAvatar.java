package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for updating an avatar
 *
 * @param newAvatar New Avatar
 */
public record UpdateAvatar(int newAvatar) implements Serializable {
}
