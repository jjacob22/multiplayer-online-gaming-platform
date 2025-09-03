package game.tic_tac_toe;

import game.Piece;
import game.tic_tac_toe.pieces.X;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeModelTest {

    TicTacToeModel model = new TicTacToeModel();

    @Test
    void isWin() {

    }

    /**
     * Try to place a piece where there was one already placed
     * Note that standard functionality for placePiece is thoroughly tested in TicTacToeServerControllerTest.java
     * Here, we only test exceptions that didn't come up there
     */

    @Test
    void placePiece() {
        model = new TicTacToeModel();
        X x = new X();
        model.placePiece(1, 1, x);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> model.placePiece(1, 1, x));

    }
}