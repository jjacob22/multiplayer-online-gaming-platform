package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for sending requests to move a piece on a game board.
 * @param fromRow the current row location of the piece to move
 * @param fromCol the current column location of the piece to move
 * @param toRow the column location of where the player would like to move the piece
 * @param toCol the row location of where the player would like to move the piece
 */
public record MovePiece(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) implements Serializable {
}
