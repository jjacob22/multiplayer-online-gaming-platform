package networking.server_events;

import java.util.EventObject;

public class ServerExceptionEvent extends EventObject {
    private final String exceptionMessage;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ServerExceptionEvent(Object source, String exceptionMessage) {
        super(source);
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
