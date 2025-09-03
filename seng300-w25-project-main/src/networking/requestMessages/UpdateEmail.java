package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for updating user email
 *
 * @param newEmail New Email
 * @param verificationCodeProvided 2-factor authentication
 */
public record UpdateEmail(String newEmail, String verificationCodeProvided) implements Serializable {
}
