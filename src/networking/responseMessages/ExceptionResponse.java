package networking.responseMessages;

import java.io.Serializable;

/**
 * A record object for error message
 *
 * @param errorMessage Error Message
 */
public record ExceptionResponse(String errorMessage) implements Serializable,Response {
}
