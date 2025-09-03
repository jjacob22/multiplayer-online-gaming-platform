package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for sending requests to place a piece somewhere on a game board.
 * @param row the row location to place the piece
 * @param col the column location to place the piece
 */
public record PlacePiece(Integer row, Integer col) implements Serializable {
}
