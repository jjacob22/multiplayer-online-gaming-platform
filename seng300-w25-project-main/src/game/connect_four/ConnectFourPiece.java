package game.connect_four;

import game.Piece;

public abstract class ConnectFourPiece extends Piece {
    public static final char HOLLOW_CIRCLE = '○';
    public static final char SOLID_CIRCLE = '●';
    public static final int TEAM_1 = 1;
    public static final int TEAM_2 = 2;

    public ConnectFourPiece(int row, int col, char symbol, long ownerID) {
        super( row, col, symbol, ownerID );
    }
    //Tostring, add in each sub class R B
}
