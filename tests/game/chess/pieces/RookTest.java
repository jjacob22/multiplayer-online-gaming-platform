package game.chess.pieces;

import game.chess.ChessModel;
import game.chess.ChessPiece;
import game.chess.ChessServerController;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.MovePiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    /*
   Valid Movement Testing
    */
    @Test
    void isValidWhiteHorizontalLeft() {
        //Tests that the rook can move sideways (white)
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 5,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        MovePiece m = new MovePiece(6,7,6,0);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 0, wRook.getCol() );
        assertEquals( 6, wRook.getRow() );
    }
    @Test
    void isValidWhiteHorizontalRight() {
        //Tests that the rook can move sideways (white)
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 6,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,0, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        MovePiece m = new MovePiece(6,0,6,7);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 7, wRook.getCol() );
        assertEquals( 6, wRook.getRow() );
    }
    @Test
    void isValidWhiteVerticalUp() {
        //Tests that the rook can move upwards
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 5,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        //ystem.out.println(wRook.isValid( 5,0,6,0,model ));
        //model.movePiece(6,0,wRook );
        MovePiece m = new MovePiece(6,7,0,7);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 7, wRook.getCol() );
        assertEquals( 0, wRook.getRow() );
    }
    @Test
    void isValidWhiteVerticalDown() {
        //Tests that the rook can move upwards
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 6,7,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        //ystem.out.println(wRook.isValid( 5,0,6,0,model ));
        //model.movePiece(6,0,wRook );
        MovePiece m = new MovePiece(6,7,5,7);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 7, wRook.getCol() );
        assertEquals( 5, wRook.getRow() );
    }
    @Test
    void isValidWhiteFail() {
        // Check for a diagonal move which should result in the rook not moving
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 6,6,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,6, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
       MovePiece m = new MovePiece(6,6,5,5);
       GameAction action = new GameAction( m, 1 );
       GameState g = controllerCopy.handleInput( action );
        assertEquals( 6, wRook.getCol() );
        assertEquals( 6, wRook.getRow() );
    }

    @Test
    void isValidBlackHorizontalLeft() {
        //Tests that the rook can move sideways (white)
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,7,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 2, wpawn.getCol() );
        assertEquals( 1, wpawn.getRow() );
        m = new MovePiece(6,7,6,0);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 0, bRook.getCol() );
       assertEquals( 6, bRook.getRow() );
    }
    @Test
    void isValidBlackHorizontalRight() {
        //Tests that the rook can move sideways (white)
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,0,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,0, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

         m = new MovePiece(6,0,6,7);
        action= new GameAction( m, 2 );
         g = controllerCopy.handleInput( action );
        assertEquals( 7, bRook.getCol() );
        assertEquals( 6, bRook.getRow() );
    }
    @Test
    void isValidBlackVerticalUp() {
        //Tests that the rook can move upwards
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,7,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        //ystem.out.println(wRook.isValid( 5,0,6,0,model ));
        //model.movePiece(6,0,wRook );
        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        m = new MovePiece(6,7,0,7);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 7, bRook.getCol() );
        assertEquals( 0, bRook.getRow() );
    }
    @Test
    void isValidBlackVerticalDown() {
        //Tests that the rook can move upwards
        ChessModel model = new ChessModel();

        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,7,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        //ystem.out.println(wRook.isValid( 5,0,6,0,model ));
        //model.movePiece(6,0,wRook );
        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        m = new MovePiece(6,7,5,7);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 7, bRook.getCol() );
        assertEquals( 5, bRook.getRow() );
    }
    @Test
    void isValidBlackFail() {
        // Check for a diagonal move which should result in the rook not moving
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,6,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,6, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

         m = new MovePiece(6,6,5,5);
         action = new GameAction( m, 2 );
         controllerCopy.handleInput( action );
        assertEquals( 6, bRook.getCol() );
        assertEquals( 6, bRook.getRow() );
    }

    /*
      Valid Attack Movement Testing
       */
    @Test
    void isValidAttackWhiteHorizontalLeft() {
        //Tests that the rook can move sideways (white)
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 5,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        Pawn bpawn2 = new Pawn(6,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.setPieceAt( 6,0, bpawn2 );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;
        model.blackPieceArray[11] = bpawn2;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        MovePiece m = new MovePiece(6,7,6,0);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 0, wRook.getCol() );
        assertEquals( 6, wRook.getRow() );

        assertNotEquals( g.board()[6][0], bpawn2.getSymbol() );
    }
    @Test
    void isValidAttackWhiteHorizontalRight() {
        //Tests that the rook can move sideways (white)
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 6,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        Pawn bpawn2 = new Pawn(6,0,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,0, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.setPieceAt( 6,7, bpawn2 );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;
        model.blackPieceArray[11] = bpawn2;
        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        MovePiece m = new MovePiece(6,0,6,7);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 7, wRook.getCol() );
        assertEquals( 6, wRook.getRow() );
        assertNotEquals( g.board()[6][7], bpawn2.getSymbol() );
    }
    @Test
    void isValidAttackWhiteVerticalUp() {
        //Tests that the rook can move upwards
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 5,0,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        Pawn bpawn2 = new Pawn(0,7,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;
        model.blackPieceArray[11] = bpawn2;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        //ystem.out.println(wRook.isValid( 5,0,6,0,model ));
        //model.movePiece(6,0,wRook );
        MovePiece m = new MovePiece(6,7,0,7);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 7, wRook.getCol() );
        assertEquals( 0, wRook.getRow() );
        assertNotEquals( g.board()[6][7], bpawn2.getSymbol() );
    }
    @Test
    void isValidAttackWhiteVerticalDown() {
        //Tests that the rook can move upwards
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 6,7,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        Pawn bpawn2 = new Pawn(5,7,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.setPieceAt( 5,7, bpawn2 );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;
        model.blackPieceArray[11] = bpawn2;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        //ystem.out.println(wRook.isValid( 5,0,6,0,model ));
        //model.movePiece(6,0,wRook );
        MovePiece m = new MovePiece(6,7,5,7);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 7, wRook.getCol() );
        assertEquals( 5, wRook.getRow() );
        assertNotEquals( g.board()[5][7], bpawn2.getSymbol() );
    }
    @Test
    void isValidAttackWhiteFail() {
        // Check for a diagonal move which should result in the rook not moving
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Rook wRook = new Rook( 6,6,ChessPiece.TEAM_1 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        Pawn bpawn = new Pawn(1,1,ChessPiece.TEAM_2);
        Pawn bpawn2 = new Pawn(5,5,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,6, wRook);
        model.setPieceAt( 1,1, bpawn );
        model.setPieceAt( 5,5, bpawn2 );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wRook;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bpawn;
        model.blackPieceArray[11] = bpawn2;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(6,6,5,5);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );
        assertEquals( 6, wRook.getCol() );
        assertEquals( 6, wRook.getRow() );
        assertNotEquals( g.board()[6][7], bpawn2.getSymbol() );
    }
    @Test
    void isValidAttackBlackHorizontalLeft() {
        //Tests that the rook can move sideways (white)
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);
        Pawn wpawn2 = new Pawn(6,0,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,7,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.setPieceAt( 6,0 , wpawn2 );


        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.whitePieceArray[11] = wpawn;

        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        assertEquals( 2, wpawn.getCol() );
        assertEquals( 1, wpawn.getRow() );
        m = new MovePiece(6,7,6,0);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 0, bRook.getCol() );
        assertEquals( 6, bRook.getRow() );
        assertNotEquals( g.board()[6][0], wpawn2.getSymbol() );
    }
    @Test
    void isValidAttackBlackHorizontalRight() {
        //Tests that the rook can move sideways (white)
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);
        Pawn wpawn2 = new Pawn(6,7,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,0,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,0, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.setPieceAt( 6,7, wpawn2 );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.whitePieceArray[11] = wpawn2;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        m = new MovePiece(6,0,6,7);
        action= new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 7, bRook.getCol() );
        assertEquals( 6, bRook.getRow() );
        assertNotEquals( g.board()[6][7], wpawn2.getSymbol() );
    }
    @Test
    void isValidAttackBlackVerticalUp() {
        //Tests that the rook can move upwards
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);
        Pawn wpawn2 = new Pawn(0,7,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,7,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.setPieceAt( 0,7, wpawn2 );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.whitePieceArray[11] = wpawn2;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;


        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        //ystem.out.println(wRook.isValid( 5,0,6,0,model ));
        //model.movePiece(6,0,wRook );
        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        m = new MovePiece(6,7,0,7);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 7, bRook.getCol() );
        assertEquals( 0, bRook.getRow() );
        assertNotEquals( g.board()[0][7], wpawn2.getSymbol() );
    }
    @Test
    void isValidAttackBlackVerticalDown() {
        //Tests that the rook can move upwards
        ChessModel model = new ChessModel();

        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);
        Pawn wpawn2 = new Pawn(5,7,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,7,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);
        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,7, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.setPieceAt( 5,7, wpawn2 );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.whitePieceArray[11] = wpawn2;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );
        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        m = new MovePiece(6,7,5,7);
        action = new GameAction( m, 2 );
        g = controllerCopy.handleInput( action );
        assertEquals( 7, bRook.getCol() );
        assertEquals( 5, bRook.getRow() );
        assertNotEquals( g.board()[5][7], wpawn2.getSymbol() );
    }
    @Test
    void isValidAttackBlackFail() {
        // Check for a diagonal move which should result in the rook not moving
        ChessModel model = new ChessModel();
        King wking = new King(7,4, ChessPiece.TEAM_1);
        Pawn wpawn = new Pawn(2,2,ChessPiece.TEAM_1);
        Pawn wpawn2 = new Pawn(5,5,ChessPiece.TEAM_1);

        Rook bRook = new Rook( 6,6,ChessPiece.TEAM_2 );
        King bking = new King(0,4,ChessPiece.TEAM_2);

        model.setPieceAt( 7,4, wking);
        model.setPieceAt( 0,4, bking);
        model.setPieceAt( 6,6, bRook);
        model.setPieceAt( 2,2, wpawn );
        model.setPieceAt( 5,5, wpawn2 );
        model.whitePieceArray[9] = wking;
        model.whitePieceArray[10] = wpawn;
        model.whitePieceArray[11] = wpawn2;
        model.blackPieceArray[9] = bking;
        model.blackPieceArray[10] = bRook;
        // model.PieceArray[10] = p;

        ChessServerController controllerCopy = new ChessServerController(model);
        controllerCopy.addPlayer( 1 );
        controllerCopy.addPlayer( 2 );

        MovePiece m = new MovePiece(2,2,1,2);
        GameAction action = new GameAction( m, 1 );
        GameState g = controllerCopy.handleInput( action );

        m = new MovePiece(6,6,5,5);
        action = new GameAction( m, 2 );
        controllerCopy.handleInput( action );
        assertEquals( 6, bRook.getCol() );
        assertEquals( 6, bRook.getRow() );
        assertNotEquals( g.board()[5][5], wpawn2.getSymbol() );
    }

}