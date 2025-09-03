package networking.responseMessages;

import java.io.Serializable;

/**
 * A record object for a boolean response from the server
 *
 * @param bool Boolean value
 */
public record BooleanResponse(boolean bool) implements Serializable,Response {
}
