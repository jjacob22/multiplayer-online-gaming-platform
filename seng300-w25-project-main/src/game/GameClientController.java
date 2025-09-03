package game;

@Deprecated
public abstract class GameClientController {
    private long playerID;
    private String MatchID;
    private Piece[][] board;

    public GameClientController(long playerID) {
        this.playerID = playerID;
    }

    public long getPlayerID() {
        return playerID;
    }

    public String getMatchID() {
        return MatchID;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public abstract void updateBoard();

    public abstract boolean isTurn();

    public abstract void sendInput(int[] input);
}
