package game.chess.pieces;

import game.GameModel;
import game.chess.ChessModel;
import game.chess.ChessPiece;
import game.chess.ChessServerController;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.MovePiece;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    @Test
    void isFirstMoveWhite() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);

        Pawn bpawn = new Pawn(1,1, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 1,1, bpawn);
        model.setPieceAt(6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,4,1);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 1, pawn.getCol() );
        assertEquals( 4, pawn.getRow() );
    }
    @Test
    void isFirstMoveBlack() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(1,1, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 1,1, bpawn);
        model.setPieceAt(6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,4,1);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(1,1,3,1);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 1, bpawn.getCol() );
        assertEquals( 3, bpawn.getRow() );
    }

    @Test
    void isNotFirstMoveWhite() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 2, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(1,1, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 1,1, bpawn);
        model.setPieceAt(6, 2, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,2,4,2);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(1,1,3,1);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        m = new MovePiece(4,2,2,2);
        action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 2, pawn.getCol() );
        assertEquals( 4, pawn.getRow() );
    }
    @Test
    void isNotFirstMoveBlack() {
        //Try to move a black piece twice when it isnt the first move
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 2, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(3,3, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 3,3, bpawn);
        model.setPieceAt(6, 2, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,2,4,2);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(3,3,5,3);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 3, bpawn.getCol() );
        assertEquals( 3, bpawn.getRow() );
    }

    @Test
    void isValidWhitePawn() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(1,1, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt(0 ,4, bking);
        model.setPieceAt( 1,1, bpawn);
        model.setPieceAt(6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,5,1);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 1, pawn.getCol() );
        assertEquals( 5, pawn.getRow() );
    }
    @Test
    void isValidBlackPawn() {
        //test that black pawns can move one square from starting position
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(1,1, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 1,1, bpawn);
        model.setPieceAt(6, 1, pawn);
        model.whitePieceArray[9] =    wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,5,1);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(1,1,2,1);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 1, bpawn.getCol() );
        assertEquals( 2, bpawn.getRow() );
    }
    @Test
    void isValidWhitePawnNotStart() {
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(3, 1, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(1,0, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 1,0, bpawn);
        model.setPieceAt(3, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(3,1,1,1);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        assertEquals( 1, pawn.getCol() );
        assertEquals( 3, pawn.getRow() );
    }
    @Test
    void isValidBlackPawnNotStart() {
        //test that black pawns can move one square from starting position
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(4,0, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 4,0, bpawn);
        model.setPieceAt(6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,5,1);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(4,0,5,0);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 0, bpawn.getCol() );
        assertEquals( 5, bpawn.getRow() );
    }

    @Test
    void attackWithWhitePawn(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(5,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,2, bpawn);
        model.setPieceAt( 6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,5,2);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        assertEquals( 2, pawn.getCol() );
        assertEquals( 5, pawn.getRow() );
    }
    @Test
    void attackWithBlackPawn(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(5,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,2, bpawn);
        model.setPieceAt( 6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,6,4);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        m = new MovePiece(5,2,6,1);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 1, bpawn.getCol() );
        assertEquals( 6, bpawn.getRow() );
    }


    @Test
    void attackKnightWithW(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Knight bk = new Knight(5,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,2, bk);
        model.setPieceAt( 6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bk;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,5,2);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        assertEquals( 2, pawn.getCol() );
        assertEquals( 5, pawn.getRow() );

    }
    @Test
    void attackRookWithW(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Rook br = new Rook(5,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,2, br);
        model.setPieceAt( 6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = br;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,5,2);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        assertEquals( 2, pawn.getCol() );
        assertEquals( 5, pawn.getRow() );

    }
    @Test
    void attackBishopWithW(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Bishop bb = new Bishop(5,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,2, bb);
        model.setPieceAt( 6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bb;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,5,2);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        assertEquals( 2, pawn.getCol() );
        assertEquals( 5, pawn.getRow() );

    }
    @Test
    void attackQueenWithW(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn pawn = new Pawn(6, 1, ChessPiece.TEAM_1);
        Queen bq = new Queen(5,2, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 5,2, bq);
        model.setPieceAt( 6, 1, pawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = pawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bq;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,1,5,2);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        assertEquals( 2, pawn.getCol() );
        assertEquals( 5, pawn.getRow() );

    }

    @Test
    void attackKnightWithB(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Knight wk = new Knight(4,4, ChessPiece.TEAM_1);

        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(3, 3, ChessPiece.TEAM_2);
        model.setPieceAt( 3, 3, bpawn);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 4,4, wk);
        model.setPieceAt( 3, 3, bpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = bpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = wk;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(3,3,4,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 4, bpawn.getCol() );
        assertEquals( 4, bpawn.getRow() );

    }
    @Test
    void attackRookWithB(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wr = new Rook(4,4, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(3, 3, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 4,4, wr);
        model.setPieceAt( 3, 3, bpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = bpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = wr;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(3,3,4,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 4, bpawn.getCol() );
        assertEquals( 4, bpawn.getRow() );

    }

    @Test
    void attackBishopWithB(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Bishop wb = new Bishop(4,4, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(3, 3, ChessPiece.TEAM_2);
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 4,4, wb);
        model.setPieceAt( 3, 3, bpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = bpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = wb;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(3,3,4,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 4, bpawn.getCol() );
        assertEquals( 4, bpawn.getRow() );

    }
    @Test
    void attackQueenWithB(){
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Queen wq = new Queen(4,4, ChessPiece.TEAM_1);
        Pawn bpawn = new Pawn(3, 3, ChessPiece.TEAM_2);
        King bking = new King(0,5,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,5, bking);
        model.setPieceAt( 4,4, wq);
        model.setPieceAt( 3, 3, bpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = bpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = wq;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(3,3,4,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 4, bpawn.getCol() );
        assertEquals( 4, bpawn.getRow() );

    }

    @Test
    void attackBlackFailedDirection(){
        // tests wrong direction movement, pawn should not move
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Knight wk = new Knight(2,4, ChessPiece.TEAM_1);

        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(3, 3, ChessPiece.TEAM_2);
        model.setPieceAt( 3, 3, bpawn);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 2,4, wk);
        model.setPieceAt( 3, 3, bpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = bpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = wk;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(3,3,4,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 3, bpawn.getCol() );
        assertEquals( 3, bpawn.getRow() );

    }
    @Test
    void attackWhiteFailedDirection(){
        // tests wrong direction movement, pawn should not move
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Knight bk = new Knight(2,4, ChessPiece.TEAM_2);

        King bking = new King(0,7,ChessPiece.TEAM_2);
        Pawn wpawn = new Pawn(1, 3, ChessPiece.TEAM_1);
        model.setPieceAt( 1, 3, wpawn);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,7, bking);
        model.setPieceAt( 2,4, bk);
        model.setPieceAt( 3, 3, wpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = wpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bk;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(1,3,2,4);
        GameAction action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 3, wpawn.getCol() );
        assertEquals( 3, wpawn.getRow() );

    }
    @Test
    void attackFailedNoPieceWithWhite(){
        // tests wrong direction movement, pawn should not move
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);

        King bking = new King(0,7,ChessPiece.TEAM_2);
        Pawn wpawn = new Pawn(1, 3, ChessPiece.TEAM_1);
        model.setPieceAt( 1, 3, wpawn);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,7, bking);
        model.setPieceAt( 3, 3, wpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = wpawn;
        model.blackPieceArray[9] = bking;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(1,3,2,4);
        GameAction action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 3, wpawn.getCol() );
        assertEquals( 3, wpawn.getRow() );

    }
    @Test
    void attackFailedNoPieceWithBlack(){
        // tests wrong direction movement, pawn should not move
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(3, 3, ChessPiece.TEAM_2);
        model.setPieceAt( 3, 3, bpawn);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 3, 3, bpawn);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = bpawn;
        model.blackPieceArray[9] = bking;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,7,3);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );

        m = new MovePiece(3,3,4,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 3, bpawn.getCol() );
        assertEquals( 3, bpawn.getRow() );

    }

}