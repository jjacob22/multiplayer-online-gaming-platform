package game.tic_tac_toe;

import game.GameClientController;

@Deprecated
public class TicTacToeClientController extends GameClientController {
    public TicTacToeClientController(long playerID) {
        super(playerID);
    }

    @Override
    public void updateBoard() {

    }

    @Override
    public boolean isTurn() {
        return false;
    }

    @Override
    public void sendInput(int[] input) {

    }
}
