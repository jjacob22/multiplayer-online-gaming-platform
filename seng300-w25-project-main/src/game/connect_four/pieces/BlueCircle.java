package game.connect_four.pieces;

import game.connect_four.ConnectFourPiece;

public class BlueCircle extends ConnectFourPiece {
    public BlueCircle(int row, int col) {
        super( row, col, HOLLOW_CIRCLE, TEAM_1 );
    }
}
