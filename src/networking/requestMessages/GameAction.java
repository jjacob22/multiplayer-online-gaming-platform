package networking.requestMessages;

import java.io.Serializable;

public record GameAction(Record action, Integer playerID) implements Serializable {
}
