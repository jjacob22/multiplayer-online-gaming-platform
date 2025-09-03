package networking.responseMessages;

import java.io.Serializable;

/**
 * A record object for an object response from the server
 *
 * @param data Object data
 */
public record ObjectResponse(Object data) implements Serializable,Response {
}
