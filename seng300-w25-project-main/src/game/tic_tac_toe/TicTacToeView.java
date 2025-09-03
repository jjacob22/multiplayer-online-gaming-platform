package game.tic_tac_toe;

import game.PlayerController;
import game.TerminalView;

import java.util.Scanner;

@Deprecated
public class TicTacToeView extends TerminalView {
    PlayerController player;

    TicTacToeView(TicTacToeServerController game, PlayerController player) {
        super(game);
        this.player = player;
    }

    @Override
    public void run() {
        return;
    }

    /*

    @Override
    public void run() {
        while (game.isPlaying()) {
            if (game.getCurrentPlayer() == player) {
                printBoard(game.getBoard());
                boolean trying = true;
                while (trying) {
                    int x, y;
                    System.out.printf("Please enter two numbers for the coordinates of where you would like to place your piece: \n");
                    Scanner scanner = new Scanner(System.in);
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    try {
                        TicTacToeServerController thisGame = (TicTacToeServerController) this.game;
                        thisGame.placePiece(x, y);
                        trying = false;
                        System.out.println("You placed the piece at (" + x + ", " + y + "). Waiting for next player...");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Please try again.");
                    }
                }
            }
        }
    }

     */
}
