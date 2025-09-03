package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for creating a new account
 *
 * @param email Email Address
 * @param password Password
 * @param user Username
 * @param confirmPass Confirmed Password
 */
public record VerifySignup(String code, String email, String password, String user, String confirmPass) implements Serializable {
}
