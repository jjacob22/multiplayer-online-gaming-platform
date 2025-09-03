package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for user Log In
 *
 * @param username Username
 * @param password Password
 */
public record LogIn(String username, String password) implements Serializable {
}
