package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for requests to update a user's profile.
 * @param bio info
 */
public record UpdateBio(String bio) implements Serializable {
}
