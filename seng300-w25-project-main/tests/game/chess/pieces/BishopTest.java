package game.chess.pieces;

import game.chess.ChessModel;
import game.chess.ChessPiece;
import game.chess.ChessServerController;
import networking.requestMessages.GameAction;
import networking.requestMessages.MovePiece;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class BishopTest {

    @Test
    void isValidRightMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4,ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(1, 1, ChessPiece.TEAM_1);
        Bishop bishop = new Bishop(7,6, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,6, bishop);
        model.setPieceAt(1, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = bishop;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = pawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,6,4,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 3, bishop.getCol() );
        assertEquals( 4, bishop.getRow() );
    }
    @Test
    void changeColorMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,0,ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(1, 0, ChessPiece.TEAM_1);
        Bishop bishop = new Bishop(7,6, ChessPiece.TEAM_1);
        King bking = new King(0,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,0, wking);
        model.setPieceAt( 0,0, bking);
        model.setPieceAt( 1,0, pawn);
        model.setPieceAt( 7,6, bishop);
        model.whitePieceArray[0] = wking;
        model.blackPieceArray[0] = bking;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,6,7,7);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 6, bishop.getCol() );
        assertEquals( 7, bishop.getRow() );
    }
    @Test
    void pieceInTheWayMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,0,ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(5, 4, ChessPiece.TEAM_1);
        Bishop bishop = new Bishop(7,6, ChessPiece.TEAM_1);
        King bking = new King(0,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,0, wking);
        model.setPieceAt( 0,0, bking);
        model.setPieceAt( 5,4, pawn);
        model.setPieceAt( 7,6, bishop);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = bishop;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,6,4,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 6, bishop.getCol() );
        assertEquals( 7, bishop.getRow() );
    }
    @Test
    void wrongDiagonalMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,0,ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(5, 4, ChessPiece.TEAM_1);
        Bishop bishop = new Bishop(7,6, ChessPiece.TEAM_1);
        King bking = new King(0,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,0, wking);
        model.setPieceAt( 0,0, bking);
        model.setPieceAt( 5,4, pawn);
        model.setPieceAt( 7,6, bishop);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = bishop;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,6,1,2);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 6, bishop.getCol() );
        assertEquals( 7, bishop.getRow() );
    }
    @Test
    void takingAPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,0,ChessPiece.TEAM_1);
        Bishop bishop = new Bishop(7,6, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(1, 0, ChessPiece.TEAM_1);
        King bking = new King(0,0,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(5, 4, ChessPiece.TEAM_2);
        model.setPieceAt( 7,0, wking);
        model.setPieceAt( 0,0, bking);
        model.setPieceAt( 5,4, wpawn);
        model.setPieceAt( 5,4, bpawn);
        model.setPieceAt( 7,6, bishop);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = bishop;
        model.whitePieceArray[1] = wpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,6,5,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 5, bishop.getRow() );
        assertEquals( 4, bishop.getCol() );
    }
}