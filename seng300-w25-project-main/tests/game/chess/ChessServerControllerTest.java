package game.chess;

import game.GameStatus;
import game.Piece;
import game.chess.pieces.King;
import game.chess.pieces.Knight;
import game.chess.pieces.Pawn;
import game.chess.pieces.Queen;
import game.tic_tac_toe.pieces.O;
import match_making.Game;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.MovePiece;
import  networking.requestMessages.*;
import networking.requestMessages.OfferDraw;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ChessServerControllerTest {
    @Test
    void testOfferDrawDeclined() {
        // Test a declined draw
        ChessServerController controller = new ChessServerController();
        controller.addPlayer( 6 );
        controller.addPlayer( 7 );

        OfferDraw d = new OfferDraw();
        GameAction a = new GameAction( d, 6 );
        controller.handleInput( a );
        //MovePiece m = new MovePiece( 6, 6, 5, 6 );
        MovePiece m = new MovePiece( 7, 6, 5, 5 ); // test works for knight movement
        GameAction a2 = new GameAction( m, 6 );
        controller.handleInput(a2);
        MovePiece m2 = new MovePiece( 1, 1, 2, 1  );
        GameAction a3 = new GameAction( m2, 7 );
        controller.handleInput(a3);
        GameAction a4 = new GameAction( d, 7 );
        GameState g = controller.handleInput(a4);
        assertNotEquals( GameStatus.DRAW, g.status(),"board" + Arrays.deepToString( g.board() ) );

    }
    @Test
    void testOfferDrawAccepted() {
        // Test an accepted draw
        ChessServerController controller = new ChessServerController();
        controller.addPlayer( 6 );
        controller.addPlayer( 7 );

        OfferDraw d = new OfferDraw();
        GameAction a = new GameAction( d, 6 );
        controller.handleInput( a );
        MovePiece m = new MovePiece( 6, 1, 5, 1 );
        GameAction a2 = new GameAction( m, 6 );
        controller.handleInput(a2);

        GameAction a3 = new GameAction( d, 7 );
        GameState g = controller.handleInput(a3);
        assertEquals( GameStatus.DRAW, g.status() );
    }
    @Test
    void testPawnPromotion() {
        ChessServerController controller = new ChessServerController();

        // Setup players
        controller.addPlayer(1); // assume playerID 1 is team 1 (white)
        controller.addPlayer(2); // playerID 2 is team 2 (black)

        // White pawn moves to promotion row
        MovePiece moveToPromotion = new MovePiece(6, 0, 7, 0); // white pawn moves from (6,0) to (7,0)
        GameAction movePawnToPromotion = new GameAction(moveToPromotion, 1);
        GameState stateAfterMove = controller.handleInput(movePawnToPromotion);

        // At this point, the promotion should be pending,
        // thus it should still be player 1's turn awaiting promotion
        assertEquals(GameStatus.PLAYING, stateAfterMove.status(),
                "Game should still be in progress awaiting promotion");

        // Try a move from player 2, which should fail due to promotion pending
        MovePiece blackInvalidMove = new MovePiece(1, 0, 2, 0);
        GameAction invalidMoveDuringPromotion = new GameAction(blackInvalidMove, 2);
        GameState stateAfterInvalidMove = controller.handleInput(invalidMoveDuringPromotion);

        // It should still be player 1's turn, since promotion wasn't done yet
        assertEquals(stateAfterMove.players().current().getPlayerID(), stateAfterInvalidMove.players().current().getPlayerID(),
                "State should be unchanged when another player moves during pending promotion");

        // Now, player 1 finally promotes the pawn
        PromotePawn promoteToQueen = new PromotePawn(PromotePawn.Promotion.Queen);
        GameAction promotionAction = new GameAction(promoteToQueen, 1);
        GameState stateAfterPromotion = controller.handleInput(promotionAction);

        // After promotion, turn should now advance to player 2.
        // by performing a valid player 2 move now to check if it gets processed.
        MovePiece blackValidMove = new MovePiece(1, 1, 2, 1);
        GameAction validMoveAfterPromotion = new GameAction(blackValidMove, 2);
        GameState stateAfterBlackMove = controller.handleInput(validMoveAfterPromotion);

        // Game should accept this move now, meaning promotion process was successfully finished
        assertNotEquals(stateAfterPromotion, stateAfterBlackMove,
                "State should change after a valid move from player 2 post-promotion");
    }
    @Test
    void testMovesCorrectlyWithController() {
        ChessServerController controller = new ChessServerController();

        controller.addPlayer(1);
        controller.addPlayer(2);

        // White moves pawn from (6,4) to (5,4)
        MovePiece w = new MovePiece(6, 4, 5, 4);
        GameAction wAction = new GameAction(w, 1);
        GameState gW = controller.handleInput(wAction);
        assertTrue(gW.board()[5][4].getSymbol() == Pawn.PAWN_SOLID);

        // Black moves pawn from (1,4) to (2,4)
        MovePiece b = new MovePiece(1, 4, 2, 4);
        GameAction bAction = new GameAction(b, 2);
        GameState gB = controller.handleInput(bAction);

        assertTrue(gB.board()[2][4].getSymbol() == Pawn.PAWN_HOLLOW);
    }
    @Test
    void whiteKnightIsNotBlackKnight() {
        ChessServerController controller = new ChessServerController();

        controller.addPlayer(1);
        controller.addPlayer(2);

        // White moves knight from (7,6) to (5,5)
        MovePiece move = new MovePiece(7, 6, 5, 5);
        GameAction Action = new GameAction(move, 1);
        GameState g = controller.handleInput(Action);

        // Black moves pawn from (1,4) to (2,4)
        move = new MovePiece(1, 4, 2, 4);
        Action = new GameAction(move, 2);
        g = controller.handleInput(Action);

        // White moves pawn from (6,6) to (5,6)
        move = new MovePiece(6, 6, 5, 6);
        Action = new GameAction(move, 1);
        g = controller.handleInput(Action);

        assertEquals(g.status(), GameStatus.PLAYING);

    }

    @Test
    void blackKnightIsNotWhiteKnight() {
        ChessServerController controller = new ChessServerController();

        controller.addPlayer(1);
        controller.addPlayer(2);

        // White moves pawn from (6,4) to (5,4)
        MovePiece move = new MovePiece(6, 4, 5, 4);
        GameAction Action = new GameAction(move, 1);
        GameState g = controller.handleInput(Action);

        // Black moves knight from (0,6) to (2,5)
        move = new MovePiece(0, 6, 2, 5);
        Action = new GameAction(move, 2);
        g = controller.handleInput(Action);

        // White moves another pawn from (6,6) to (5,6)
        move = new MovePiece(6, 6, 5, 6);
        Action = new GameAction(move, 1);
        g = controller.handleInput(Action);

        assertEquals(g.status(), GameStatus.PLAYING);

    }
    @Test
    void kingInCheck() {
        ChessServerController controller = new ChessServerController();

        controller.addPlayer(1);
        controller.addPlayer(2);

        // White moves pawn from (6,4) to (5,4)
        MovePiece move = new MovePiece(7, 6, 5, 7);
        GameAction Action = new GameAction(move, 1);
        GameState g = controller.handleInput(Action);

        // Black moves pawn from (1,4) to (2,4)
        move = new MovePiece(1, 1, 2, 1);
        Action = new GameAction(move, 2);
        g = controller.handleInput(Action);

        move = new MovePiece(5, 7, 4, 5);
        Action = new GameAction(move, 1);
        g = controller.handleInput(Action);

        move = new MovePiece(2, 1, 3, 1);
        Action = new GameAction(move, 2);
        g = controller.handleInput(Action);

        move = new MovePiece(4, 5, 3, 7);
        Action = new GameAction(move, 1);
        g = controller.handleInput(Action);

        move = new MovePiece(3, 1, 4, 1);
        Action = new GameAction(move, 2);
        g = controller.handleInput(Action);

        assertTrue(g.board()[4][1].getSymbol() == Pawn.PAWN_HOLLOW);

        move = new MovePiece(3, 7, 2, 5);
        Action = new GameAction(move, 1);
        g = controller.handleInput(Action);

        move = new MovePiece(4, 1, 5, 1);
        Action = new GameAction(move, 2);
        g = controller.handleInput(Action);

        assertTrue(g.board()[2][5].getSymbol() == Knight.KNIGHT_SOLID);

        assertTrue(g.board()[4][1].getSymbol() == Pawn.PAWN_HOLLOW);
        assertNull(g.board()[5][1]);
        assertTrue(g.players().current().getOwnerID() == ChessPiece.TEAM_2);

    }
    @Test
    void testEndGame(){
        ChessModel model = new ChessModel();
        ChessServerController controller = new ChessServerController(model);
        Queen wqueen = new Queen( 5,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4, ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 5,0, wqueen);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[10] = wqueen;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        controller.addPlayer( 1 );
        controller.addPlayer( 2 );

        MovePiece m = new MovePiece(5,0,6,0);
        GameAction action = new GameAction( m, 1 );
        GameState g = controller.handleInput( action );

        assertEquals(GameStatus.WIN, g.status(),"board" + Arrays.deepToString( g.board() ));
        assertEquals(2, g.players().current().getPlayerID());

    }

    @Test
    void addPlayerTest(){
        ChessServerController controller = new ChessServerController();
        controller.addPlayer(55);
        controller.addPlayer(66);
        assertEquals(controller.getPlayerIDs(),new ArrayList<Integer>(Arrays.asList(55,66)));
    }

}