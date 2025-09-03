package networking.requestMessages;

import match_making.Game;

import java.io.Serializable;

public record RequestHosts(Game game) implements Serializable {
}
