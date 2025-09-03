package game.connect_four;

import game.GameStatus;
import game.Piece;
import game.chess.pieces.Pawn;
import game.connect_four.pieces.BlueCircle;
import game.connect_four.pieces.RedCircle;
import networking.requestMessages.ForfeitMatch;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.PlacePiece;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
class ConnectFourServerControllerTest {
    public ConnectFourServerController controller;
    @Test
    void addPlayer() {

        controller = new ConnectFourServerController();

        //controller.addPlayer( 4 );
        assertThrows(IllegalStateException.class, () -> {controller.addPlayer( 1 );
            controller.addPlayer( 2 );
            controller.addPlayer( 3 );}  );
    }
    /**
     * Tests what happens when the same player tries to join twice
     */
    @Test
    void addPlayer2() {
        // start by adding player with userID 1
        controller = new ConnectFourServerController(new int[]{1});

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
        controller = new ConnectFourServerController(new int[]{5});
        // remove player 5
        controller.removePlayer(5);
        // now check to make sure that player 5 is gone
        assertEquals(controller.getPlayerIDs(), new ArrayList<>());
    }

    @Test
    void handleInputMoveDraw() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle redCircle = new RedCircle( 5,1 );
        BlueCircle bCircle = new BlueCircle( 5,1 );
        model.setPieceAt( 5,0, redCircle );
        model.setPieceAt( 5,1, bCircle );
        model.setPieceAt( 5,2, redCircle );
        model.setPieceAt( 5,3, bCircle );
        model.setPieceAt( 5,4, redCircle );
        model.setPieceAt( 5,5, bCircle );
        model.setPieceAt( 5,6, redCircle );

        model.setPieceAt( 4,0, bCircle );
        model.setPieceAt( 4,1, redCircle );
        model.setPieceAt( 4,2, bCircle );
        model.setPieceAt( 4,3, redCircle );
        model.setPieceAt( 4,4, bCircle );
        model.setPieceAt( 4,5, redCircle );
        model.setPieceAt( 4,6, bCircle );

        model.setPieceAt( 3,0, bCircle );
        model.setPieceAt( 3,1, redCircle );
        model.setPieceAt( 3,2, bCircle );
        model.setPieceAt( 3,3, redCircle );
        model.setPieceAt( 3,4, bCircle );
        model.setPieceAt( 3,5, redCircle );
        model.setPieceAt( 3,6, bCircle );

        model.setPieceAt( 2,0, redCircle );
        model.setPieceAt( 2,1, bCircle );
        model.setPieceAt( 2,2, redCircle );
        model.setPieceAt( 2,3, bCircle );
        model.setPieceAt( 2,4, redCircle );
        model.setPieceAt( 2,5, bCircle );
        model.setPieceAt( 2,6, redCircle );

        model.setPieceAt( 1,0, redCircle );
        model.setPieceAt( 1,1, bCircle );
        model.setPieceAt( 1,2, redCircle );
        model.setPieceAt( 1,3, bCircle );
        model.setPieceAt( 1,4, redCircle );
        model.setPieceAt( 1,5, bCircle );
        model.setPieceAt( 1,6, redCircle );

        model.setPieceAt( 0,0, bCircle );
        model.setPieceAt( 0,1, redCircle );
        model.setPieceAt( 0,2, bCircle );
        model.setPieceAt( 0,3, redCircle );
        model.setPieceAt( 0,4, bCircle );
        model.setPieceAt( 0,5, redCircle );
        model.setPieceAt( 0,6, bCircle );

        ConnectFourServerController controller2 = new ConnectFourServerController(model);
        controller2.addPlayer( 1 );
        controller2.addPlayer( 2 );

        Piece [][] board = model.getBoard();
        PlacePiece p = new PlacePiece(5,1);
        GameAction a = new GameAction( p, 1 );
        GameState g = controller2.handleInput( a );
        assertEquals( g.status(), GameStatus.DRAW);
    }

    @Test
    void handleInputMoveWin() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle redCircle = new RedCircle( 5,1 );
        BlueCircle bCircle = new BlueCircle( 5,1 );

        model.setPieceAt( 0,0, redCircle );
        model.setPieceAt( 0,1, null );
        model.setPieceAt( 0,2, redCircle );
        model.setPieceAt( 0,3, bCircle );
        model.setPieceAt( 0,4, redCircle );
        model.setPieceAt( 0,5, bCircle );
        model.setPieceAt( 0,6, redCircle );

        model.setPieceAt( 1,0, redCircle );
        model.setPieceAt( 1,1, redCircle );
        model.setPieceAt( 1,2, redCircle );
        model.setPieceAt( 1,3, bCircle );
        model.setPieceAt( 1,4, redCircle );
        model.setPieceAt( 1,5, bCircle );
        model.setPieceAt( 1,6, redCircle );

        model.setPieceAt( 2,0, bCircle );
        model.setPieceAt( 2,1, redCircle );
        model.setPieceAt( 2,2, bCircle );
        model.setPieceAt( 2,3, redCircle );
        model.setPieceAt( 2,4, bCircle );
        model.setPieceAt( 2,5, redCircle );
        model.setPieceAt( 2,6, bCircle );

        model.setPieceAt( 3,0, bCircle );
        model.setPieceAt( 3,1, redCircle );
        model.setPieceAt( 3,2, bCircle );
        model.setPieceAt( 3,3, redCircle );
        model.setPieceAt( 3,4, bCircle );
        model.setPieceAt( 3,5, redCircle );
        model.setPieceAt( 3,6, bCircle );

        model.setPieceAt( 4,0, redCircle );
        model.setPieceAt( 4,1, bCircle );
        model.setPieceAt( 4,2, redCircle );
        model.setPieceAt( 4,3, bCircle );
        model.setPieceAt( 4,4, redCircle );
        model.setPieceAt( 4,5, bCircle );
        model.setPieceAt( 4,6, redCircle );

        model.setPieceAt( 5,0, redCircle );
        model.setPieceAt( 5,1, bCircle );
        model.setPieceAt( 5,2, redCircle );
        model.setPieceAt( 5,3, bCircle );
        model.setPieceAt( 5,4, redCircle );
        model.setPieceAt( 5,5, bCircle );
        model.setPieceAt( 5,6, redCircle );

        ConnectFourServerController controller2 = new ConnectFourServerController(model);
        controller2.addPlayer( 1 );
        controller2.addPlayer( 2 );

        Piece [][] board = model.getBoard();
        PlacePiece p = new PlacePiece(0,1);
        GameAction a = new GameAction( p, 1 );
        GameState g = controller2.handleInput( a );
        assertEquals( GameStatus.WIN, g.status() );
    }


    @Test
    void handleInputMoveMoving() {
        ConnectFourServerController controller2 = new ConnectFourServerController();
        controller2.addPlayer( 1 );
        controller2.addPlayer( 2 );
        ConnectFourModel model = new ConnectFourModel();
        RedCircle redCircle = new RedCircle( 5,1 );
        model.setPieceAt( 5,1, redCircle );
        Piece [][] board = model.getBoard();
        PlacePiece p = new PlacePiece(5,1);
        GameAction a = new GameAction( p, 1 );
        GameState g = controller2.handleInput( a );
        assertEquals( g.board()[5][1].getSymbol() , board[5][1].getSymbol() );
    }
    @Test
    void handleInputMoveNotTest() {
        ConnectFourServerController controller2 = new ConnectFourServerController();
        controller2.addPlayer( 1 );
        controller2.addPlayer( 2 );
        ConnectFourModel model = new ConnectFourModel();
        BlueCircle blCircle = new BlueCircle( 5,1 );
        model.setPieceAt( 5,1, blCircle );
        Piece [][] board = model.getBoard();
        PlacePiece p = new PlacePiece(5,1);
        GameAction a = new GameAction( p, 2 );
        GameState g = controller2.handleInput( a );
        assertNull(g.board()[5][1] );
    }





    @Test
    void handleInputForfeit() {
        ConnectFourServerController controller2 = new ConnectFourServerController();
        controller2.addPlayer( 1 );
        controller2.addPlayer( 2 );
        PlacePiece p = new PlacePiece(5,1);
        GameAction a = new GameAction( p, 1 );
        controller2.handleInput( a );
        ForfeitMatch f = new ForfeitMatch();
        GameAction a2 = new GameAction( f, 1 );
        GameState g = controller2.handleInput( a2 );
        assertEquals( GameStatus.WIN, g.status() );
        assertEquals( 2, g.players().current().getPlayerID() );
    }

    @Test
    void handleInputForfeit2() {
        ConnectFourServerController controller2 = new ConnectFourServerController();
        controller2.addPlayer( 1 );
        controller2.addPlayer( 2 );
        ForfeitMatch f = new ForfeitMatch();
        GameAction a2 = new GameAction( f, 1 );
        GameState g = controller2.handleInput( a2 );
        assertEquals( GameStatus.WIN, g.status() );
        assertEquals( 2, g.players().current().getPlayerID() );
    }
    @Test
    void testPlacePieceSuccessfully() {
        ConnectFourServerController controller = new ConnectFourServerController();
        controller.addPlayer(1); // Player 1
        controller.addPlayer(2); // Player 2

        // player 1 placing a piece in column 3
        GameAction action1 = new GameAction(new PlacePiece(0, 3), 1); // Row should be ignored in Connect Four
        GameState state1 = controller.handleInput(action1);

        // Check the piece is placed at the bottom of the column
        assertEquals(ConnectFourPiece.SOLID_CIRCLE, state1.board()[5][3].getSymbol());

        //player 2 placing a piece in column 3
        GameAction action2 = new GameAction(new PlacePiece(0, 3), 2);
        GameState state2 = controller.handleInput(action2);

        // Check the piece is placed on top of the first piece in the same column
        // Assuming the next available spot from bottom
        assertEquals(ConnectFourPiece.HOLLOW_CIRCLE, state2.board()[4][3].getSymbol());
        assertEquals(GameStatus.PLAYING, state2.status(), "Game should still be in progress");
    }

}