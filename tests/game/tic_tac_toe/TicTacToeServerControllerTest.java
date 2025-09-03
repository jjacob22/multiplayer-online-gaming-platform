package game.tic_tac_toe;

import game.GameStatus;
import game.Piece;
import game.connect_four.ConnectFourModel;
import game.connect_four.ConnectFourServerController;
import game.connect_four.pieces.BlueCircle;
import game.connect_four.pieces.RedCircle;
import game.tic_tac_toe.pieces.O;
import game.tic_tac_toe.pieces.X;
import networking.requestMessages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeServerControllerTest {

    TicTacToeServerController controller;
    @BeforeEach
    void setUp(){
        controller = new TicTacToeServerController();
    }

    /**
     * Tests to ensure the correct IDs are added when adding players
     */
    @Test
    void addPlayer1() {
        // start by adding two players
        controller.addPlayer(55);
        controller.addPlayer(66);

        // make sure both players are in the list
        assertEquals(controller.getPlayerIDs(),new ArrayList<Integer>(Arrays.asList(55,66)));

    }

    /**
     * Tests what happens when you try to add too many players
     */
    @Test
    void addPlayer2() {
        // start by adding two players so that we are at max capacity
        controller = new TicTacToeServerController(new int[]{1,2});

        // when we try to add another player, it should throw an IllegalStateException
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->controller.addPlayer(3) );

        // double check that the error message is correct
        String expectedMessage = "No more players may join the game.";
        assertEquals(exception.getMessage(),expectedMessage);
    }

    /**
     * Tests what happens when the same player tries to join twice
     */
    @Test
    void addPlayer3() {
        // start by adding player with userID 1
        controller.addPlayer(1);

        // when we try to add another player with userID 1 (aka the same player) then we want an exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->controller.addPlayer(1) );

        // double check that the error message is correct
        String expectedMessage = "Player already joined.";
        assertEquals(exception.getMessage(),expectedMessage);

    }


    /**
     * Tests that a player is removed successfully
     */
    @Test
    void removePlayer() {
        // add player 5
        controller.addPlayer(5);
        // remove player 5
        controller.removePlayer(5);
        // now check to make sure that player 5 is gone
        assertEquals(controller.getPlayerIDs(), new ArrayList<>());
    }

    /**
     * Tests standard input handling, just makes sure that the state is correct and everything if input is correct
     */
    @Test
    void handleInput1() {
        controller = new TicTacToeServerController(new int[]{1,2});
        PlacePiece p = new PlacePiece(2,2);
        GameAction a = new GameAction(p, 1);
        GameState g = controller.handleInput(a);
        assertEquals(g.status(), GameStatus.PLAYING);
    }

    /**
     * Test that you cannot play when not your turn
     */
    @Test
    void handleInput2() {
        controller = new TicTacToeServerController(new int[]{1,2});
        PlacePiece p = new PlacePiece(1,1);
        GameAction a = new GameAction(p, 2);

        // make sure an exception is thrown when not your turn
        Exception exception = assertThrows(IllegalStateException.class, () -> controller.handleInput(a));

        // double check that the error message is correct
        String expectedMessage = "It is not your turn!";
        assertEquals(exception.getMessage(),expectedMessage);
    }

    /**
     * Tests the case when an action is attempted but the game is over
     */
    @Test
    void handleInput3() {
        controller = new TicTacToeServerController(new int[]{1,2});

        // the order that the cells will be filled
        int[] rows = new int[]{0,0,0,1,1,1,2,2,2};
        int[] cols = new int[]{0,1,2,1,0,2,1,0,2};

        // make all the moves until the board is full
        for (int i=0;i<rows.length;i++){
            controller.handleInput(makeGameAction(rows[i],cols[i],(i%2+1)));
        }

        // try to take an extra move
        Exception exception = assertThrows(IllegalStateException.class, () -> controller.handleInput(makeGameAction(1,1,1)));

        // double check that the error message is correct
        String expectedMessage = "The game is over!";
        assertEquals(exception.getMessage(),expectedMessage);
    }

    /**
     * Tests the case where there is a draw. Makes sure that the status is correct.
     */
    @Test
    void handleInputMove4() {
        controller = new TicTacToeServerController(new int[]{1,2});

        // order that the cells are filled
        int[] rows = new int[]{0,0,0,1,1,1,2,2};
        int[] cols = new int[]{0,1,2,1,0,2,1,0};

        // make all the moves
        for (int i=0;i<rows.length;i++){
            controller.handleInput(makeGameAction(rows[i],cols[i],(i%2+1)));
        }

        // check that it ends in a draw
        GameState gameState = controller.handleInput(makeGameAction(2,2,1));
        assertEquals(GameStatus.DRAW, gameState.status());
    }

    /**
     * Tests the case where a player forfeits.
     */
    @Test
    void handleInputMove5() {
        controller = new TicTacToeServerController(new int[]{1,2});

        // order that the cells are filled
        int[] rows = new int[]{0,0,0,1,1,1,2,2};
        int[] cols = new int[]{0,1,2,1,0,2,1,0};

        // make all the moves
        for (int i=0;i<rows.length;i++){
            controller.handleInput(makeGameAction(rows[i],cols[i],(i%2+1)));
        }

        ForfeitMatch f = new ForfeitMatch();

        GameState g = controller.handleInput(new GameAction(f,1));

        assertEquals( GameStatus.WIN, g.status() );
        assertEquals( 2, g.players().current().getPlayerID() );
    }

    /**
     * Tests the case where a player makes an invalid move
     */
    @Test
    void handleInputMove6() {
        controller = new TicTacToeServerController(new int[]{1,2});

        OfferDraw offerDraw = new OfferDraw();
        // try to take an extra move
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.handleInput(new GameAction(offerDraw,1)));

        // double check that the error message is correct
        String expectedMessage = "Invalid action";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
    /**
     * Tests the case where someone places a winning piece
     */
    @Test
    void handleInputMove7() {
        controller = new TicTacToeServerController(new int[]{1,2});

        // order that the cells are filled
        int[] rows = new int[]{0,1,0,1};
        int[] cols = new int[]{0,0,1,1};

        // make all the moves
        for (int i=0;i<rows.length;i++){
            controller.handleInput(makeGameAction(rows[i],cols[i],(i%2+1)));
        }

        // check that it ends with a win when you place three in a row
        GameState gameState = controller.handleInput(makeGameAction(0,2,1));
        assertEquals(GameStatus.WIN, gameState.status());
    }

    /**
     * A helper function for taking actions in one line
     * @param row is the row coordinate
     * @param col is the column coordinate
     * @param playerID is the player taking their turn
     * @return the GameAction that it results in
     */
    private GameAction makeGameAction(int row, int col, int playerID){
        PlacePiece p = new PlacePiece(row,col);
        return new GameAction(p, playerID);
    }
}
