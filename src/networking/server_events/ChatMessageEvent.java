package networking.server_events;

import java.util.EventObject;

public class ChatMessageEvent extends EventObject {
    private final String sender;
    private final String message;

    /**
     * Constructs an event with a message.
     *
     * @param source The object on which the Event initially occurred.
     * @param message The message received.
     * @throws IllegalArgumentException if source is null
     */
    public ChatMessageEvent(Object source, String sender, String message) {
        super(source);
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}

