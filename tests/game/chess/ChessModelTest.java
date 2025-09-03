package game.chess;

import game.GameModel;
import game.GameStatus;
import game.Piece;
import match_making.Game;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.MovePiece;
import game.chess.ChessPiece;
import game.chess.pieces.*;
import networking.requestMessages.PromotePawn;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ChessModelTest {
    //public static ChessServerController controller = new ChessServerController();
    private ChessModel chessModel;
    @BeforeEach
    void setUp() {
        chessModel = new ChessModel();
        chessModel.initializePieces(); //set up the board in an initial configuration
    }
    @Test
    void initializePieces() {
    }

    @Test
    void movePiece() {

    }
    @Test
    void castlingWhiteKingMoveTest() {
        ChessModel model = new ChessModel();
        Rook castle = new Rook(7,7,ChessPiece.TEAM_1);
        King king = new King(6,4,ChessPiece.TEAM_1);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,7, castle );
        model.setPieceAt( 6,4, king );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.whitePieceArray[10] = castle;
        model.blackPieceArray[9] = king2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 23 );
        controllerCopy.addPlayer( 22 );

        MovePiece m = new MovePiece(6,4,7,4);
        GameAction action = new GameAction( m, 23 );
        controllerCopy.handleInput( action );
        m = new MovePiece(7,4,7,6);
        action = new GameAction( m, 23 );
        controllerCopy.handleInput( action );

        assertEquals( 7, castle.getCol() );
        assertEquals( 4, king.getCol() );
    }
    @Test
    void castlingWhiteRookMoveTest() {
        ChessModel model = new ChessModel();
        Rook castle = new Rook(6,7,ChessPiece.TEAM_1);
        King king = new King(7,4,ChessPiece.TEAM_1);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 6,7, castle );
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.whitePieceArray[10] = castle;
        model.blackPieceArray[9] = king2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 23 );
        controllerCopy.addPlayer( 22 );


        MovePiece m = new MovePiece(6,7,7,7);
        GameAction action = new GameAction( m, 23 );
        controllerCopy.handleInput( action );
        m = new MovePiece(7,4,7,6);
        action = new GameAction( m, 23 );
        controllerCopy.handleInput( action );

        assertEquals( 7, castle.getCol() );
        assertEquals( 4, king.getCol() );
    }
    @Test
    void castlingWhiteRightTest() {
        ChessModel model = new ChessModel();
        Rook castle = new Rook(7,7,ChessPiece.TEAM_1);

        King king = new King(7,4,ChessPiece.TEAM_1);

        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,7, castle );
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.whitePieceArray[10] = castle;
        model.blackPieceArray[9] = king2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 23 );
        controllerCopy.addPlayer( 22 );

        MovePiece m = new MovePiece(7,4,7,6);
        GameAction action = new GameAction( m, 23 );
        controllerCopy.handleInput( action );


        assertEquals( 5, castle.getCol() );
        assertEquals( 6, king.getCol() );
    }
    @Test
    void castlingWhiteLeftTest() {
        ChessModel model = new ChessModel();
        Rook castle = new Rook(7,0,ChessPiece.TEAM_1);
        King king = new King(7,4,ChessPiece.TEAM_1);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,0, castle );
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.whitePieceArray[10] = castle;
        model.blackPieceArray[9] = king2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 23 );
        controllerCopy.addPlayer( 22 );


        MovePiece m = new MovePiece(7,4,7,2);
        GameAction action = new GameAction( m, 23 );
        controllerCopy.handleInput( action );


        assertEquals( 2, king.getCol() );
        assertEquals( 3, castle.getCol() );
    }
    @Test
    void castlingBlackKingMoveTest() {
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Rook castle = new Rook(0,7,ChessPiece.TEAM_2);
        King king2 = new King(1,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, castle );
        model.setPieceAt( 1,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = castle;
        model.blackPieceArray[9] = king2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,6,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(1,4,0,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        m = new MovePiece(6,4,7,4);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        m = new MovePiece(0,4,0,6);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 7, castle.getCol() );
        assertEquals( 4, king2.getCol() );
    }
    @Test
    void castlingBlackRookMoveTest() {
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Rook castle = new Rook(1,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 1,7, castle );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = castle;
        model.blackPieceArray[9] = king2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,6,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(1,7,0,7);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        m = new MovePiece(6,4,7,4);
        action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(0,4,0,6);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 7, castle.getCol() );
        assertEquals( 4, king2.getCol() );
    }
    @Test
    void castlingBlackRightTest() {
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Rook castle = new Rook(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, castle );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = castle;
        model.blackPieceArray[9] = king2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,6,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );


        m = new MovePiece(0,4,0,6);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );

        assertEquals( 5, castle.getCol() );
        assertEquals( 6, king2.getCol() );
    }
    @Test
    void castlingBlackLeftTest() {
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        Rook castle = new Rook(0,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,4, king2 );
        model.setPieceAt( 0,0, castle );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = castle;
        model.blackPieceArray[9] = king2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(7,4,6,4);
        GameAction action = new GameAction( m, 1 );
        controllerCopy.handleInput( action );
        m = new MovePiece(0,4,0,2);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );


        assertEquals( 2, king2.getCol() );
        assertEquals( 3, castle.getCol() );
    }





    @Test
    void isUnderAttackTest1(){
       //To see what we are working with, check square beside a rook attacks
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Rook castle = new Rook(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, castle );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = castle;
        model.blackPieceArray[9] = king2;
        assertTrue(model.isUnderAttack( 0,6,ChessPiece.TEAM_1 ));

    }
    @Test
    void isUnderAttackTest2(){
        //To see what we are working with, check square the rook attacks
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Rook castle = new Rook(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, castle );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = castle;
        model.blackPieceArray[9] = king2;
        assertFalse(model.isUnderAttack( 0,7,ChessPiece.TEAM_1 ));

    }

    @Test
    void isUnderAttackTest3(){
        //To see what we are working with, check square the rook attacks
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Queen q  = new Queen(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, q );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = q;
        model.blackPieceArray[9] = king2;
        assertFalse(model.isUnderAttack( 0,7,ChessPiece.TEAM_1 ));
    }
    @Test
    void isUnderAttackTest4(){
        //To see what we are working with, check square the rook attacks
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Bishop b  = new Bishop(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, b );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = b;
        model.blackPieceArray[9] = king2;
        assertFalse(model.isUnderAttack( 0,7,ChessPiece.TEAM_1 ));
    }
    @Test
    void isUnderAttackTest5(){
        //To see what we are working with, check square the rook attacks
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Pawn p  = new Pawn(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, p );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = p;
        model.blackPieceArray[9] = king2;
        assertFalse(model.isUnderAttack( 0,7,ChessPiece.TEAM_1 ));
    }
    @Test
    void isUnderAttackTest6(){
        //To see what we are working with, check square the rook attacks
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Knight K  = new Knight(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, K );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = K;
        model.blackPieceArray[9] = king2;
        assertFalse(model.isUnderAttack( 0,7,ChessPiece.TEAM_1 ));
    }
    @Test
    void isUnderAttackTest7(){
        //To see what we are working with, check square the rook attacks
        ChessModel model = new ChessModel();

        King king = new King(7,4,ChessPiece.TEAM_1);
        Pawn  p = new Pawn(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 2,7, p );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = p;
        model.blackPieceArray[9] = king2;
        assertFalse(model.isUnderAttack ( 3,6,ChessPiece.TEAM_1 ));
    }
    @Test
    void isUnderAttackTest8(){
        //To see what we are working with, check square the rook attacks
        ChessModel model = new ChessModel();
        King king = new King(7,4,ChessPiece.TEAM_1);
        Pawn  p = new Pawn(6,3,ChessPiece.TEAM_2);
        King king2 = new King(0,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 6,3, p );
        model.setPieceAt( 0,0, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = p;
        model.blackPieceArray[9] = king2;
        assertTrue(model.isUnderAttack ( 7,2,ChessPiece.TEAM_1 ));
    }

    @Test
    void isUATQueenMovementDown(){
        ChessModel model = new ChessModel();
            //To see what we are working with, check square the rook attacks
            King king = new King(7,4,ChessPiece.TEAM_1);
            Queen q  = new Queen(0,7,ChessPiece.TEAM_2);
            King king2 = new King(0,4,ChessPiece.TEAM_2);
            model.setPieceAt( 7,4, king );
            model.setPieceAt( 0,7, q );
            model.setPieceAt( 0,4, king2 );
            model.whitePieceArray[9] = king;
            model.blackPieceArray[10] = q;
            model.blackPieceArray[9] = king2;
            assertTrue(model.isUnderAttack( 7,0,ChessPiece.TEAM_1 ));
    }
    @Test
    void isUATQueenMovementUp(){
        ChessModel model = new ChessModel();
        //To see what we are working with, check square the rook attacks
        King king = new King(7,4,ChessPiece.TEAM_1);
        Queen q  = new Queen(3,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 3,7, q );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = q;
        model.blackPieceArray[9] = king2;
        assertTrue(model.isUnderAttack( 7,7,ChessPiece.TEAM_1 ));
    }
    @Test
    void isUATQueenMovementLeft(){
        ChessModel model = new ChessModel();
        //To see what we are working with, check square the rook attacks
        King king = new King(7,4,ChessPiece.TEAM_1);
        Queen q  = new Queen(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, q );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = q;
        model.blackPieceArray[9] = king2;
        assertTrue(model.isUnderAttack( 0,0,ChessPiece.TEAM_1 ));
    }

    @Test
    void isUATQueenMovementDia(){
        ChessModel model = new ChessModel();
        //To see what we are working with, check square the rook attacks
        King king = new King(7,4,ChessPiece.TEAM_1);
        Queen q  = new Queen(0,7,ChessPiece.TEAM_2);
        King king2 = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, king );
        model.setPieceAt( 0,7, q );
        model.setPieceAt( 0,4, king2 );
        model.whitePieceArray[9] = king;
        model.blackPieceArray[10] = q;
        model.blackPieceArray[9] = king2;
        assertTrue(model.isUnderAttack( 7,0,ChessPiece.TEAM_1 ));
    }


//    @Test
//    void isStalemate() {
//        ChessModel model = new ChessModel();
//
//
//        King blackKing = new King(0, 7, ChessPiece.TEAM_2);
//        Queen whiteQueen = new Queen(1, 5, ChessPiece.TEAM_1);
//        King whiteKing = new King(0, 0, ChessPiece.TEAM_1);
//
//        model.setPieceAt(0, 7, blackKing);
//        model.setPieceAt(1, 1, whiteQueen);
//        model.setPieceAt(2, 6, whiteKing);
//
//        model.blackPieceArray[0] = blackKing;
//        model.whitePieceArray[0] = whiteQueen;
//        model.whitePieceArray[1] = whiteKing;
//
//
//        assertTrue(model.isStalemate());
//    }



    @Test
    void isCheckmateTest2() {
        ChessModel model = new ChessModel();

        King wking = new King(0, 0, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(6, 6, ChessPiece.TEAM_1);
        Pawn wpawn2 = new Pawn(6, 5, ChessPiece.TEAM_1);
        Rook wrook = new Rook(5, 6, ChessPiece.TEAM_1);


        King bking = new King(7, 7, ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(6, 7, ChessPiece.TEAM_2);


        model.setPieceAt(0, 0, wking);
        model.setPieceAt(6, 6, wpawn);
        model.setPieceAt(6, 5, wpawn2);
        model.setPieceAt(5, 6, wrook);


        model.setPieceAt(7, 7, bking);
        model.setPieceAt(6, 7, bpawn);


        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = wpawn;
        model.whitePieceArray[2] = wpawn2;
        model.whitePieceArray[3] = wrook;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bpawn;


      //  boolean whiteCheckmate = model.isCheckmate(ChessPiece.TEAM_1);
       // boolean blackCheckmate = model.isCheckmate(ChessPiece.TEAM_2);


       // assertEquals(false, whiteCheckmate, "White should not be in checkmate.");
        //assertEquals(true, blackCheckmate, "Black should be in checkmate.");
    }



    @Test
    void isCheckmateTest() {
        ChessModel model = new ChessModel();

        King whiteKing = new King(7, 4, ChessPiece.TEAM_1);
        model.setPieceAt(7, 4, whiteKing);
        model.whitePieceArray[9] = whiteKing;

        Rook blackRook1 = new Rook(6, 3, ChessPiece.TEAM_2);
        Rook blackRook2 = new Rook(6, 5, ChessPiece.TEAM_2);
        Queen blackQueen = new Queen(5, 4, ChessPiece.TEAM_2);
        King blackKing = new King(0, 4, ChessPiece.TEAM_2);

        model.setPieceAt(6, 3, blackRook1);
        model.setPieceAt(6, 5, blackRook2);
        model.setPieceAt(5, 4, blackQueen);
        model.setPieceAt(0, 4, blackKing);

        model.blackPieceArray[0] = blackRook1;
        model.blackPieceArray[1] = blackRook2;
        model.blackPieceArray[2] = blackQueen;
        model.blackPieceArray[9] = blackKing;

        //assertEquals(true, model.isCheckmate(ChessPiece.TEAM_1));
    }

    @Test
    void isDraw() {
        // Insufficient Material
        ChessModel model1 = new ChessModel();
        King whiteKing1 = new King(7, 4, ChessPiece.TEAM_1);
        Knight whiteKnight1 = new Knight(6, 3, ChessPiece.TEAM_1);
        King blackKing1 = new King(0, 4, ChessPiece.TEAM_2);
        model1.setPieceAt(7, 4, whiteKing1);
        model1.setPieceAt(6, 3, whiteKnight1);
        model1.setPieceAt(0, 4, blackKing1);
        model1.whitePieceArray[9] = whiteKing1;
        model1.whitePieceArray[10] = whiteKnight1;
        model1.blackPieceArray[9] = blackKing1;
        assertEquals(true, model1.isDraw());

        // Fifty-Move Rule
        ChessModel model2 = new ChessModel();
        King whiteKing2 = new King(7, 4, ChessPiece.TEAM_1);
        King blackKing2 = new King(0, 4, ChessPiece.TEAM_2);
        model2.setPieceAt(7, 4, whiteKing2);
        model2.setPieceAt(0, 4, blackKing2);
        model2.whitePieceArray[9] = whiteKing2;
        model2.blackPieceArray[9] = blackKing2;
        ArrayList<String> moveList2 = GameModel.getMoveList();
        moveList2.clear();
        for (int i = 0; i < 50; i++) {
            moveList2.add("Kf1");
            moveList2.add("Kf8");
        }
        assertEquals(true, model2.isDraw());

        // Threefold Repetition
        ChessModel model3 = new ChessModel();
        King whiteKing3 = new King(7, 4, ChessPiece.TEAM_1);
        King blackKing3 = new King(0, 4, ChessPiece.TEAM_2);
        model3.setPieceAt(7, 4, whiteKing3);
        model3.setPieceAt(0, 4, blackKing3);
        model3.whitePieceArray[9] = whiteKing3;
        model3.blackPieceArray[9] = blackKing3;
        ArrayList<String> moveList3 = GameModel.getMoveList();
        moveList3.clear();
        moveList3.add("Ke1"); moveList3.add("Ke8");
        moveList3.add("Ke1"); moveList3.add("Ke8");
        moveList3.add("Ke1"); moveList3.add("Ke8");
        assertEquals(true, model3.isDraw());
    }



    @Test
    void isAwaitingPromotion(){
       //simulate a pawn is one move away from promoting
        Pawn pawn =(Pawn) chessModel.getPieceAt(6,7); //assume pawn is from team 1 and started at index 6, it should promote in row 0. i set it to row1 so that the move will not be blocked
        chessModel.setPieceAt(1,7,pawn); // set the piece  1 move away from promotion
        chessModel.setPieceAt(0,7,null); //set this to null to allow pawn movement
        chessModel.movePiece(0,7,pawn);
        assertTrue(chessModel.isAwaitingPromotion(),"Pawn should be in promotion row");
    }
    //check if promotion works, i.e the chosen piece is at the new location and the flag is removed
    @Test
    void pawnPromotion() {
        Pawn pawn = (Pawn) chessModel.getPieceAt(1,0); //pawn should be team 2 now
        chessModel.setPieceAt(6,0,pawn); //set the pawn one move away from promotion
        chessModel.setPieceAt(7,0,null);//clear path
        chessModel.movePiece(7,0,pawn);

        //ensure pawn is initially in the array
        boolean pawnInArray = false;
        for (ChessPiece piece : chessModel.blackPieceArray) {
            if (piece.equals(pawn)) {
                pawnInArray = true;
                break;
            }
        }
        assertTrue(pawnInArray, "Original pawn should be in the array at first");

        chessModel.promotePawn(PromotePawn.Promotion.Queen);//promote to queen

        // Check if the pawn has been replaced by a Queen
        ChessPiece promotedPiece = (ChessPiece) chessModel.getPieceAt(7, 0);
        assertInstanceOf(Queen.class, promotedPiece, "Pawn should be promoted to a Queen");
        assertEquals(ChessPiece.TEAM_2, promotedPiece.getOwnerID(), "Promoted piece should have the correct team ID");

        // Ensure the original pawn is no longer in the array and replaced by the new Queen
        boolean foundOriginalPawn = false;
        for (ChessPiece piece : chessModel.blackPieceArray) {
            if (piece.equals(pawn)) {
                foundOriginalPawn = true;
                break;
            }
        }
        assertFalse(foundOriginalPawn, "Original pawn should no longer be in the array");
    }
    @Test
    void shouldNotPromotePawn(){
        // Place a pawn just before the promotion row but with a blocking piece
        Pawn pawn = (Pawn)chessModel.getPieceAt(1,0); //get a pawn from team 2
        chessModel.setPieceAt(6, 0, pawn); //objective is to go to promotion row
        ChessPiece blockingPiece = (ChessPiece) chessModel.getPieceAt(7,0); //should be a blocking peace right now at (7,0) probably a knight

        // Attempt to move the pawn into the promotion row where there is another piece blocking the way
        boolean moveSuccessful = chessModel.movePiece(7, 0, pawn);

        // Check move should fail due to blocking piece
        assertFalse(moveSuccessful, "Move should fail due to blocking piece");

        // Ensure pawn is still in its original place and not promoted
        assertSame(pawn, chessModel.getPieceAt(6, 0), "Pawn should still be at its original place");
        assertSame(blockingPiece,chessModel.getPieceAt(7, 0), "The blocking piece should prevent the pawn from moving");

        // Verify pawn is not awaiting promotion
        assertFalse(pawn.isAwaitingPromotion(), "Pawn should not be awaiting promotion");

        // Ensure the original pawn is still in the array
        boolean foundPawn = false;
        for (ChessPiece piece : chessModel.blackPieceArray) {
            if (piece.equals(pawn)) {
                foundPawn = true;
                break;
            }
        }
        assertTrue(foundPawn, "Original pawn should still be in the array");

        // Attempt to promote incorrectly
        chessModel.promotePawn(PromotePawn.Promotion.Queen);
        assertFalse(chessModel.getPieceAt(6, 0) instanceof Queen, "Pawn should not be promoted since the move was invalid");
    }
    @Test
    void whiteCheckmateTestcase1(){
        ChessModel model = new ChessModel();
        King wking = new King(7,7, ChessPiece.TEAM_1);
        King bking = new King(0,0,ChessPiece.TEAM_2);
        Queen bqeeun = new Queen( 6,7,ChessPiece.TEAM_2);
        Queen bqeeun2 = new Queen(0,6,ChessPiece.TEAM_2);
        model.setPieceAt( 7,7, wking);
        model.setPieceAt( 0,0, bking);
        model.setPieceAt( 6,7, bqeeun);
        model.setPieceAt( 0,6, bqeeun2);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bqeeun;
        model.blackPieceArray[2] = bqeeun2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        //assertFalse(model.isUnderAttack( bqeeun.getRow(),bqeeun.getCol(), 1));
        assertFalse(model.amIInCheckmate(ChessPiece.TEAM_1, model));
    }
    @Test
    void whiteCheckmateTestcase2(){
        ChessModel model = new ChessModel();
        King wking = new King(7,7, ChessPiece.TEAM_1);
        King bking = new King(0,0,ChessPiece.TEAM_2);
        Queen bqeeun = new Queen( 6,7,ChessPiece.TEAM_2);
        Queen bqeeun2 = new Queen(6,6,ChessPiece.TEAM_2);
        model.setPieceAt( 7,7, wking);
        model.setPieceAt( 0,0, bking);
        model.setPieceAt( 6,7, bqeeun);
        model.setPieceAt( 6,6, bqeeun2);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bqeeun;
        model.blackPieceArray[2] = bqeeun2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        assertTrue( model.amIInCheckmate( ChessPiece.TEAM_1, model ) );
    }
    @Test
    void blackCheckmateTestcase2(){
        ChessModel model = new ChessModel();
        King bking = new King(7,7, ChessPiece.TEAM_2);
        King wking = new King(0,0,ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 6,7,ChessPiece.TEAM_1);
        Queen wqueen2 = new Queen(6,6,ChessPiece.TEAM_1);
        model.setPieceAt( 0,0, wking);
        model.setPieceAt( 7,7, bking);
        model.setPieceAt( 6,7, wqueen);
        model.setPieceAt( 6,6, wqueen2);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = wqueen;
        model.whitePieceArray[2] = wqueen2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        assertTrue( model.amIInCheckmate( ChessPiece.TEAM_2, model ) );
    }
    @Test
    void whiteCheckmateTestcase3(){
        ChessModel model = new ChessModel();
        King wking = new King(7,7, ChessPiece.TEAM_1);
        Rook wrook = new Rook(5 ,5 , ChessPiece.TEAM_1);
        King bking = new King(0,0,ChessPiece.TEAM_2);
        Queen bqeeun = new Queen( 0,7,ChessPiece.TEAM_2);
        Queen bqeeun2 = new Queen(0,6,ChessPiece.TEAM_2);
        model.setPieceAt( 7,7, wking);
        model.setPieceAt(5, 5, wrook);
        model.setPieceAt( 0,0, bking);
        model.setPieceAt( 0,7, bqeeun);
        model.setPieceAt( 0,6, bqeeun2);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = wrook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bqeeun;
        model.blackPieceArray[2] = bqeeun2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        assertFalse( model.amIInCheckmate( ChessPiece.TEAM_1, model ) );
    }
    @Test
    void blackCheckmateTestcase3(){
        ChessModel model = new ChessModel();
        King bking = new King(7,7, ChessPiece.TEAM_2);
        Rook brook = new Rook(5 ,5 , ChessPiece.TEAM_2);
        King wking = new King(1,0,ChessPiece.TEAM_1);
        Queen wqueen = new Queen( 0,7,ChessPiece.TEAM_1);
        Queen wqueen2 = new Queen(0,6,ChessPiece.TEAM_1);
        model.setPieceAt( 7,7, bking);
        model.setPieceAt(5, 5, brook);
        model.setPieceAt( 1,0, wking);
        model.setPieceAt( 0,7, wqueen);
        model.setPieceAt( 0,6, wqueen2);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[1] = brook;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = wqueen;
        model.whitePieceArray[2] = wqueen2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        assertFalse( model.amIInCheckmate( ChessPiece.TEAM_2, model ) );
    }
    @Test
    void whiteCheckmateTestcase4(){
        ChessModel model = new ChessModel();
        King wking = new King(7,7, ChessPiece.TEAM_1);
        Rook wrook = new Rook(1 ,5 , ChessPiece.TEAM_1);
        King bking = new King(0,0,ChessPiece.TEAM_2);
        Queen bqeeun = new Queen( 1,7,ChessPiece.TEAM_2);
        Queen bqeeun2 = new Queen(0,6,ChessPiece.TEAM_2);
        model.setPieceAt( 7,7, wking);
        model.setPieceAt(1, 5, wrook);
        model.setPieceAt( 0,0, bking);
        model.setPieceAt( 1,7, bqeeun);
        model.setPieceAt( 0,6, bqeeun2);
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[1] = wrook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[1] = bqeeun;
        model.blackPieceArray[2] = bqeeun2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        assertFalse( model.amIInCheckmate( ChessPiece.TEAM_1, model ) );

    }
    @Test
    void blackCheckmateTestcase4(){
        ChessModel model = new ChessModel();
        King bking = new King(7,7, ChessPiece.TEAM_2);
        Rook brook = new Rook(1 ,5 , ChessPiece.TEAM_2);
        King wking = new King(0,0,ChessPiece.TEAM_1);
        Queen wqeeun = new Queen( 1,7,ChessPiece.TEAM_1);
        Queen wqeeun2 = new Queen(0,6,ChessPiece.TEAM_1);
        model.setPieceAt( 7,7, bking);
        model.setPieceAt(1, 5, brook);
        model.setPieceAt( 0,0, wking);
        model.setPieceAt( 1,7, wqeeun);
        model.setPieceAt( 0,6, wqeeun2);
        model.whitePieceArray[9] = wking;
        model.blackPieceArray[1] = brook;
        model.blackPieceArray[9] = bking;
        model.whitePieceArray[1] = wqeeun;
        model.whitePieceArray[2] = wqeeun2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        assertFalse( model.amIInCheckmate( ChessPiece.TEAM_2, model ) );

    }

}