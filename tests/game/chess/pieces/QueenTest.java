package game.chess.pieces;

import game.chess.ChessModel;
import game.chess.ChessPiece;
import game.chess.ChessServerController;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.MovePiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    @Test
    void validWhiteQueenVerticalDownMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,0, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,0,7,0);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 0, wqueen.getCol());
        assertEquals( 7, wqueen.getRow());
    }
    @Test
    void validBlackQueenVerticalDownMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 5,0,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,0, bqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[11] = bqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(5,0,7,0);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );

        assertEquals( 0, bqueen.getCol());
        assertEquals( 7, bqueen.getRow());
    }
    @Test
    void validWhiteQueenVerticalUpMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,0, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,0,0,0);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 0, wqueen.getCol());
        assertEquals( 0, wqueen.getRow());
    }
    @Test
    void validBlackQueenVerticalUpMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 5,0,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,0, bqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[11] = bqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(5,0,0,0);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );

        assertEquals( 0, bqueen.getCol());
        assertEquals( 0, bqueen.getRow());
    }
    @Test
    void validWhiteQueenHorizontalLeftMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,3,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,3, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,3,5,0);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 0, wqueen.getCol());
        assertEquals( 5, wqueen.getRow());
    }
    @Test
    void validBlackQueenHorizontalLeftMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 5,0,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,0, bqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[11] = bqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(5,3,5,0);
        action = new GameAction( m, 1 );
        g = controllerCopy.handleInput( action );

        assertEquals( 0, bqueen.getCol());
        assertEquals( 5, bqueen.getRow());
    }
    @Test
    void validWhiteQueenHorizontalRightMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,3,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,3, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,3,5,7);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 7, wqueen.getCol());
        assertEquals( 5, wqueen.getRow());
    }
    @Test
    void validBlackQueenHorizontalRightMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 5,0,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,0, bqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[11] = bqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;


        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(5,0,5,7);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );

        assertEquals( 5, bqueen.getRow());
        assertEquals( 7, bqueen.getCol());
    }
    @Test
    void validWhiteQueenDiagonalRightUpMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,3,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,3, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,3,1,7);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 7, wqueen.getCol());
        assertEquals( 1, wqueen.getRow());
    }
    @Test
    void validBlackQueenDiagonalRightUpMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 4,3,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 4,3, bqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[11] = bqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(4,3,0,7);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );

        assertEquals( 7, bqueen.getCol());
        assertEquals( 0, bqueen.getRow());
    }
    @Test
    void validWhiteQueenDiagonalRightDownMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,3,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,3, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,3,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 5, wqueen.getCol());
        assertEquals( 7, wqueen.getRow());
    }
    @Test
    void validBlackQueenDiagonalRightDownMove() {
        ChessModel model = new ChessModel();
        King wking = new King(0,0, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 5,3,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 0,0, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,3, bqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[11] = bqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(0,0,0,1);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(5,3,7,5);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );

        assertEquals( 5, bqueen.getCol());
        assertEquals( 7, bqueen.getRow());
    }
    @Test
    void validWhiteQueenDiagonalLeftDownMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,3,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,3, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,3,7,1);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 1, wqueen.getCol());
        assertEquals( 7, wqueen.getRow());
    }
    @Test
    void validBlackQueenDiagonalLeftDownMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 4,3,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 4,3, bqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[11] = bqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(4,3,7,0);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 0, bqueen.getCol());
        assertEquals( 7, bqueen.getRow());
    }
    @Test
    void validWhiteQueenDiagonalLeftUpMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,3,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,3, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,3,2,0);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 0, wqueen.getCol());
        assertEquals( 2, wqueen.getRow());
    }
    @Test
    void validBlackQueenDiagonalLeftUpMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 4,3,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 4,3, bqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[11] = bqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(4,3,1,0);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );

        assertEquals( 0, bqueen.getCol());
        assertEquals( 1, bqueen.getRow());
    }
    @Test
    void validWhiteQueenTakesMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 5,1,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,1, wqueen);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(5,1,1,1);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 1, wqueen.getCol());
        assertEquals( 1, wqueen.getRow());
    }
    @Test
    void validBlackQueenTakesMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen bqueen = new Queen( 5,1,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn wpawn = new Pawn(1,1,ChessPiece.TEAM_1);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,1, bqueen);
        model.setPieceAt( 1,1, wpawn );
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[10] = bqueen;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[10] = wpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(5,1,1,1);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );

        assertEquals( 1, bqueen.getCol());
        assertEquals( 1, bqueen.getRow());
    }
}