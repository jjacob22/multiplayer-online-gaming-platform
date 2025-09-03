package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for updating user password
 */
public record UpdatePassword(String username, String newPassword, String retypePassword) implements Serializable {
}
