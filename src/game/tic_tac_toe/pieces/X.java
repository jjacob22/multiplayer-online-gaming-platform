package game.tic_tac_toe.pieces;

import game.tic_tac_toe.TicTacToeModel;
import game.tic_tac_toe.TicTacToePiece;

public class X extends TicTacToePiece {
    public X(int x, int y) {
        super(x, y, TicTacToePiece.SYMBOL_X, TicTacToeModel.TEAM_1);
    }

    public X() {
        this(-1, -1);
    }
}
