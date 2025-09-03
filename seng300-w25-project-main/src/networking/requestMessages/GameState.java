package networking.requestMessages;

import game.GameStatus;
import game.Piece;
import game.PlayerController;
import game.PlayerControllerList;

import java.io.Serializable;

/**
 * A record object for sending a game state.
 * @param board the board's current state
 * @param players all players in the game
 * @param status the current status of the game
 */
public record GameState(Piece[][] board, PlayerControllerList players, GameStatus status) implements Serializable {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nGameState\n");
        for (var row : board) {
            for (var piece : row) {
                if (piece != null) {
                    builder.append(piece);
                } else {
                    builder.append("-");
                }
                builder.append(" ");
            }
            builder.append("\n");
        }
        builder.append("Players (*** is current player)\n");
        for (var player : players) {
            if (player == players.current()) {
                builder.append("*** ").append(player.getPlayerID());
            } else {
                builder.append(player.getPlayerID());
            }
            builder.append("\n");
        }
        builder.append("GameStatus\n");
        builder.append(status);
        builder.append("\n");
        return builder.toString();
    }
}
