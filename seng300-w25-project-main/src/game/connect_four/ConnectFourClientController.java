package game.connect_four;

import game.GameClientController;

@Deprecated
public class ConnectFourClientController extends GameClientController {
    public ConnectFourClientController(long playerID) {
        super( playerID );
    }

    @Override
    public void updateBoard() {
        //Get the board somehow, after sending us the inputs

    }

    @Override
    public boolean isTurn() {
        //check the playerID with the one we return
        return false;
    }

    @Override
    public void sendInput(int[] input) {
        //Send the array [action, row, col, 0,0] -last two ints would be for chess
        // 0 forfeit
        // 1 play piece
    }
}
