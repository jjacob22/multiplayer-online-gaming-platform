package networking.requestMessages;

import match_making.Game;

import java.io.Serializable;

public record GetGameStats(Game game) implements Serializable {
}
