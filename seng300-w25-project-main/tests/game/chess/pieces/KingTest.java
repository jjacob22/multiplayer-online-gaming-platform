package game.chess.pieces;

import game.chess.ChessModel;
import game.chess.ChessPiece;
import game.chess.ChessServerController;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.MovePiece;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void whiteKingValidMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 4, ChessPiece.TEAM_1);
        King bking = new King(7,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt(6, 4, pawn);
        model.setPieceAt( 7,0, bking);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;


        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );


        MovePiece m = new MovePiece(7,4,6,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );


        assertEquals( 6, wking.getRow() );
        assertEquals( 3, wking.getCol() );
    }
    @Test
    void blackKingValidMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 4, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt(6, 4, pawn);
        model.setPieceAt( 0,4, bking);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;


        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );


        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(0,4,0,5);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );


        assertEquals( 5, bking.getCol() );
        assertEquals( 0, bking.getRow() );
    }
    @Test
    void whiteMovingIntoAPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 4, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt(6, 4, pawn);
        model.setPieceAt( 0,4, bking);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;


        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );


        MovePiece m = new MovePiece(7,4,6,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );


        assertEquals( 7, wking.getRow() );
        assertEquals( 4, wking.getCol() );
    }
    @Test
    void blackMovingIntoAPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(1, 4, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt(1, 4, pawn);
        model.setPieceAt( 0,4, bking);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;


        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(0,4,1,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );


        assertEquals( 0, bking.getRow() );
        assertEquals( 4, bking.getCol() );
    }
    @Test
    void whiteTakingAPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(1, 4, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(6, 4, ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt(1, 4, wpawn);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt(6, 4, bpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = wpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,6,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 6, wking.getRow() );
        assertEquals( 4, wking.getCol() );
    }
    @Test
    void blackTakingAPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(1, 4, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(6, 4, ChessPiece.TEAM_2); // TODO error when  y =3 so pawn attacking king white
        model.setPieceAt( 7,4, wking);
        model.setPieceAt(1, 4, wpawn);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt(6, 4, bpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = wpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,6,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(0,4,1,4);
        action = new GameAction( m, 2 );
//        controllerCopy.handleInput( action );
        GameState b = controllerCopy.handleInput( action );

        assertEquals( 4, bking.getCol() );
        assertEquals( 1, bking.getRow() );
    }
    //Testing the above test without controller
    @Test
    void testingBlackTakingAPieceWithoutController() {
        //white king
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        model.setPieceAt( 7,4, wking);
        model.whitePieceArray[9] = wking;
        //white pawn
        Pawn wpawn = new Pawn(1, 4, ChessPiece.TEAM_1);
        model.setPieceAt(1, 4, wpawn);
        model.whitePieceArray[1] = wpawn;
        //black king
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 0,4, bking);
        model.blackPieceArray[9] = bking;
        //black pawn
        Pawn bpawn = new Pawn(6, 3, ChessPiece.TEAM_2);
        model.setPieceAt(6, 3, bpawn);
        model.blackPieceArray[1] = bpawn;


        assertTrue(bking.capture( 0,4,1,4,model, false, false));
        assertTrue(bking.isValid( 0,4,1,4,model,  false));

        assertTrue(model.movePiece( 1,4,bking ));
    }
    @Test
    void movingPieceTwoTimes() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(6, 4, ChessPiece.TEAM_1);
        King bking = new King(7,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt(6, 3, wpawn);
        model.setPieceAt( 7,0, bking);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = wpawn;
        model.blackPieceArray[9] = bking;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,6);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 7, wking.getRow() );
        assertEquals( 4, wking.getCol() );
    }
}