package networking.requestMessages;

import match_making.Game;

import java.io.Serializable;

public record LeaveQueue(Game game) implements Serializable {
}
