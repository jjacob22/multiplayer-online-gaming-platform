package game.connect_four.pieces;

import game.connect_four.ConnectFourPiece;

public class RedCircle extends ConnectFourPiece {
    public RedCircle(int row, int col) {
        super( row, col, SOLID_CIRCLE, TEAM_2 );
    }
}
