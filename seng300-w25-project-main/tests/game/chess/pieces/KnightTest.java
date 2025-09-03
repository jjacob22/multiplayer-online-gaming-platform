package game.chess.pieces;

import game.chess.ChessModel;
import game.chess.ChessPiece;
import game.chess.ChessServerController;
import networking.requestMessages.GameAction;
import networking.requestMessages.MovePiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

    @Test
    void whiteValidMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 4, ChessPiece.TEAM_1);
        Knight knight = new Knight(7,2, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,2, knight);
        model.setPieceAt(6, 4, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = knight;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = pawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,2,5,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 3, knight.getCol() );
        assertEquals( 5, knight.getRow() );
    }
    @Test
    void blackValidMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 4, ChessPiece.TEAM_1);
        Knight knight = new Knight(7,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,2, knight);
        model.setPieceAt(6, 4, pawn);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[10] = knight;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = pawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(7,2,5,3);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 3, knight.getCol() );
        assertEquals( 5, knight.getRow() );
    }
    @Test
    void whiteMoveOntoSameTeamPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 4, ChessPiece.TEAM_1);
        Knight knight = new Knight(7,2, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,2, knight);
        model.setPieceAt(6, 4, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = knight;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = pawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,2,6,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 7, knight.getRow() );
        assertEquals( 2, knight.getCol() );
    }
    @Test
    void blackMoveOntoSameTeamPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 4, ChessPiece.TEAM_2);
        Knight knight = new Knight(7,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,2, knight);
        model.setPieceAt(6, 4, pawn);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[10] = knight;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = pawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,2,6,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 7, knight.getRow() );
        assertEquals( 2, knight.getCol() );
    }
    @Test
    void whiteValidJumpMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 3, ChessPiece.TEAM_1);
        Pawn pawn1 = new Pawn(6, 2, ChessPiece.TEAM_1);
        Pawn pawn2 = new Pawn(5, 2, ChessPiece.TEAM_1);
        Knight knight = new Knight(7,2, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,2, knight);
        model.setPieceAt(6, 3, pawn);
        model.setPieceAt(6, 2, pawn1);
        model.setPieceAt(5, 2, pawn2);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = knight;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = pawn;
        model.whitePieceArray[2] = pawn1;
        model.whitePieceArray[3] = pawn2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,2,5,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 3, knight.getCol() );
        assertEquals( 5, knight.getRow() );
    }
    @Test
    void blackValidJumpMove() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 3, ChessPiece.TEAM_2);
        Pawn pawn1 = new Pawn(6, 2, ChessPiece.TEAM_2);
        Pawn pawn2 = new Pawn(5, 2, ChessPiece.TEAM_2);
        Knight knight = new Knight(7,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,2, knight);
        model.setPieceAt(6, 3, pawn);
        model.setPieceAt(6, 2, pawn1);
        model.setPieceAt(5, 2, pawn2);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[10] = knight;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = pawn;
        model.blackPieceArray[2] = pawn1;
        model.blackPieceArray[3] = pawn2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(7,2,5,3);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 3, knight.getCol() );
        assertEquals( 5, knight.getRow() );
    }
    @Test
    void whiteTakingPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 3, ChessPiece.TEAM_1);
        Pawn pawn1 = new Pawn(5, 3, ChessPiece.TEAM_2);
        Knight knight = new Knight(7,2, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,2, knight);
        model.setPieceAt(6, 3, pawn);
        model.setPieceAt(5, 3, pawn1);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = knight;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[2] = pawn1;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,2,5,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 3, knight.getCol() );
        assertEquals( 5, knight.getRow() );
    }
    @Test
    void blackTakingPiece() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 3, ChessPiece.TEAM_2);
        Pawn pawn1 = new Pawn(5, 3, ChessPiece.TEAM_1);
        Knight knight = new Knight(7,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 7,2, knight);
        model.setPieceAt(6, 3, pawn);
        model.setPieceAt(5, 3, pawn1);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = knight;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = pawn;
        model.whitePieceArray[2] = pawn1;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,5);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(7,2,5,3);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 3, knight.getCol() );
        assertEquals( 5, knight.getRow() );
    }
}