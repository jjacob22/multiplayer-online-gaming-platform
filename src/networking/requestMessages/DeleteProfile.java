package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for a user request to delete the profile
 *
 * @param username Username of the account to delete.
 * @param currentPassword Password of the account to delete.
 */
public record DeleteProfile(String username, String currentPassword) implements Serializable {
}
