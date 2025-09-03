package game.tic_tac_toe.pieces;

import game.tic_tac_toe.TicTacToeModel;
import game.tic_tac_toe.TicTacToePiece;

public class O extends TicTacToePiece {
    public O(int x, int y) {
        super(x, y, TicTacToePiece.SYMBOL_O, TicTacToeModel.TEAM_2);
    }

    public O() {
        this(-1, -1);
    }
}
