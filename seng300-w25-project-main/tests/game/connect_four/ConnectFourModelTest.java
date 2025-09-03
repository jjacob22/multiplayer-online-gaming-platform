package game.connect_four;

import game.Piece;
import game.connect_four.pieces.BlueCircle;
import game.connect_four.pieces.RedCircle;
import networking.requestMessages.BlockUser;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.PlacePiece;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ConnectFourModelTest {

    @Test
    void winRowSuccessful() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle( 5,1 );
        RedCircle r3 = new RedCircle( 5,3 );
        RedCircle r4 = new RedCircle( 5,4 );
        RedCircle r2 = new RedCircle( 5,2 );
        model.setPieceAt( 5,1,r );
        model.setPieceAt( 5,2,r2 );
        model.setPieceAt( 5,3,r3 );
        model.setPieceAt( 5,4,r4 );
        assertTrue( model.winRow( r2 ), "board \n" + Arrays.deepToString( model.getBoard() ) );
    }
    @Test
    void winRowFailed() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle( 5,1 );
        BlueCircle b2 = new BlueCircle( 5,2 );
        RedCircle r3 = new RedCircle( 5,3 );
        RedCircle r4 = new RedCircle( 5,4 );
        model.setPieceAt( 5,1,r );
        model.setPieceAt( 5,2,b2 );
        model.setPieceAt( 5,3,r3 );
        model.setPieceAt( 5,4,r4 );
        assertFalse( model.winRow( r4 ), "board \n" + Arrays.deepToString( model.getBoard() ) );
    }
    @Test
    void winRowFailed2() {
        //Test with blue
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle( 5,1 );
        BlueCircle b2 = new BlueCircle( 5,2 );
        RedCircle r3 = new RedCircle( 5,3 );
        RedCircle r4 = new RedCircle( 5,4 );
        model.setPieceAt( 5,1,r );
        model.setPieceAt( 5,2,b2 );
        model.setPieceAt( 5,3,r3 );
        model.setPieceAt( 5,4,r4 );
        assertFalse( model.winRow( b2 ), "board \n" + Arrays.deepToString( model.getBoard() ) );
    }
    @Test
    void testGetHorizontalLineLength(){
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle( 5,1 );
        RedCircle r3 = new RedCircle( 5,3 );
        RedCircle r4 = new RedCircle( 5,4 );
        RedCircle r2 = new RedCircle( 5,2 );
        model.setPieceAt( 5,1,r );
        model.setPieceAt( 5,2,r2 );
        model.setPieceAt( 5,3,r3 );
        model.setPieceAt( 5,4,r4 );
        model.getHorizontalLineLength( 5, 2, r2.getClass());
        assertTrue( model.getHorizontalLineLength( 5, 2, r2.getClass()) == 4 );
    }

    @Test
    void winBackwardDiagonalLeftEnd() {
        //Tests that backslash at the left end of the board will recognize a win
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle( 5,3 );
        //RedCircle rm = new RedCircle( 3,2 );
        RedCircle r2 = new RedCircle( 4,2 );
        RedCircle r3 = new RedCircle( 3,1 );
        RedCircle r4 = new RedCircle( 2,0 );
        model.setPieceAt( 5,3,r );
        model.setPieceAt( 4,2,r2 );
       // model.setPieceAt( 3,1,r3 );
        //model.setPieceAt( 2,0,r4 );

        model.setPieceAt( 3,1,r3 );
        model.setPieceAt( 2,0,r4 );

        assertTrue( model.winBackwardDiagonal( r2 ), "board \n" + Arrays.deepToString( model.getBoard() ) );

    }
    @Test
    void winBackwardDiagonalRightEnd() {
        //Tests that backslash at the right end of the board will recognize a win
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle( 5,4 );
        //RedCircle rm = new RedCircle( 3,2 );
        RedCircle r2 = new RedCircle( 4,5 );
        RedCircle r3 = new RedCircle( 3,6 );
        RedCircle r4 = new RedCircle( 2,7 );
        model.setPieceAt( 5,3,r );
        model.setPieceAt( 4,2,r2 );
        model.setPieceAt( 3,1,r3 );
        model.setPieceAt( 2,0,r4 );

        assertTrue( model.winBackwardDiagonal( r2 ), "board \n" + Arrays.deepToString( model.getBoard() ) );

    }
    @Test
    void winBackwardDiagonalMiddle() {
        //Tests for the bottom row not being 5 and that blue team can win
        ConnectFourModel model = new ConnectFourModel();
        BlueCircle r = new BlueCircle( 3,4 );
        //RedCircle rm = new RedCircle( 3,2 );
        BlueCircle r2 = new BlueCircle( 2,5 );
        BlueCircle r3 = new BlueCircle( 1,6 );
        BlueCircle r4 = new BlueCircle( 0,7 );
        model.setPieceAt( 5,3,r );
        model.setPieceAt( 4,2,r2 );
        model.setPieceAt( 3,1,r3 );
        model.setPieceAt( 2,0,r4 );

        assertTrue( model.winBackwardDiagonal( r2 ), "board \n" + Arrays.deepToString( model.getBoard() ) );

    }
    @Test
    void winBackwardDiagonalFailed() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle( 5,3 );
        //RedCircle rm = new RedCircle( 3,2 );
        RedCircle r2 = new RedCircle( 4,2 );
        RedCircle r3 = new RedCircle( 3,1 );
        //RedCircle r4 = new RedCircle( 2,0 );
        model.setPieceAt( 5,3,r );
        model.setPieceAt( 4,2,r2 );
        // model.setPieceAt( 3,1,r3 );
        //model.setPieceAt( 2,0,r4 );

        model.setPieceAt( 3,1,r3 );
        //model.setPieceAt( 2,0,r4 );

        assertFalse( model.winBackwardDiagonal( r2 ), "board \n" + Arrays.deepToString( model.getBoard() ) );

//        assertTrue(model.draw(), "Board should be in a draw state:\n" + Arrays.deepToString(model.getBoard()));
    }

    @Test
    void winforwardDiagonalSucess() { ///bottom left to top right
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle( 5,1 );
        RedCircle r2 = new RedCircle( 4,2 );
        RedCircle r3 = new RedCircle( 3,3);
        RedCircle r4 = new RedCircle( 2,4);
        model.setPieceAt( 5,1,r );
        model.setPieceAt( 4,2,r2 );
        model.setPieceAt( 3,3,r3 );
        model.setPieceAt( 2,4,r4 );
        assertTrue(model.winforwardDiagonal(new RedCircle(5, 1)), "Diagonal win should be detected.");
    }
    @Test
    void winForwardDiagonalAtEdge() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r1 = new RedCircle(3, 0);
        RedCircle r2 = new RedCircle(2, 1);
        RedCircle r3 = new RedCircle(1, 2);
        RedCircle r4 = new RedCircle(0, 3);
        model.setPieceAt(3, 0, r1);  // Start at the middle left corner of the visible board
        model.setPieceAt(2, 1, r2);
        model.setPieceAt(1, 2, r3);
        model.setPieceAt(0, 3, r4);
        assertTrue(model.winforwardDiagonal(r1), "Diagonal win at the board edge should be detected.");
    }

    @Test
    void winForwardDiagonalIncomplete() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle(5, 1);
        BlueCircle b = new BlueCircle(4, 2);
        RedCircle r2 = new RedCircle(3, 3);
        RedCircle r3 = new RedCircle(2, 4);
        model.setPieceAt(5, 1, r);
        model.setPieceAt(4, 2, b);  // Intentional break in pattern with a blue piece
        model.setPieceAt(3, 3, r2);
        model.setPieceAt(2, 4, r3);
        assertFalse(model.winforwardDiagonal(r), "Incomplete diagonal should not trigger a win.");
    }


    @Test
    void winColumnSuccessful() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle(0, 1);
        RedCircle r2 = new RedCircle(1, 1);
        RedCircle r3 = new RedCircle(2, 1);
        RedCircle r4 = new RedCircle(3, 1);
        model.setPieceAt(0, 1, r);
        model.setPieceAt(1, 1, r2);
        model.setPieceAt(2, 1, r3);
        model.setPieceAt(3, 1, r4);
        assertTrue(model.winColumn(r), "Column win should be detected for Red.");
    }

    @Test
    void winColumnInterrupted() {
        ConnectFourModel model = new ConnectFourModel();
        RedCircle r = new RedCircle(0, 2);
        BlueCircle b = new BlueCircle(1, 2); // Interruption with a Blue piece
        RedCircle r2 = new RedCircle(2, 2);
        RedCircle r3 = new RedCircle(3, 2);
        model.setPieceAt(0, 2, r);
        model.setPieceAt(1, 2, b);
        model.setPieceAt(2, 2, r2);
        model.setPieceAt(3, 2, r3);
        assertFalse(model.winColumn(r), "Interruption in column should prevent a win.");
    }

    @Test
    void checkFullTrue() {
        ConnectFourModel model = new ConnectFourModel();
        for (int i=0;i< model.ROWS;i++){
            RedCircle r = new RedCircle( i,1 ); // generate red circles in column 1 to fill it up
            model.setPieceAt(i,1,r);
        }
        assertTrue(model.checkFull(1));

    }
    @Test
    void checkFullFalse() {
        ConnectFourModel model = new ConnectFourModel();
        for (int i=0;i< model.ROWS-1;i++){ //leave last row empty
            BlueCircle r = new BlueCircle( i,1 );
        }assertFalse( model.checkFull(1));
    }

//fill the top row and then test a draw.
    @Test
    void shouldDraw() {
        ConnectFourModel model = new ConnectFourModel();
        for (int col = 0; col < ConnectFourModel.COLUMNS; col++) {//loop through every column in the top row
            Piece piece = (col % 2 == 0) ? new RedCircle(0, col) : new BlueCircle(0, col);// place red pieces at even columns of row and blue pieces at odd. hence alternating betwee the two on the board
            model.setPieceAt(0, col, piece);
        }
        //draw because board[0] should be full
        assertTrue(model.draw(), "Board should be in a draw state:\n" + Arrays.deepToString(model.getBoard()));
    }
    @Test
    void shoudNotDraw() {
        ConnectFourModel model = new ConnectFourModel();
        for (int col = 1; col < ConnectFourModel.COLUMNS; col++) {//skip first column
            Piece piece = (col % 2 == 0) ? new RedCircle(0, col) : new BlueCircle(0, col);// place red pieces at even columns of row and blue pieces at odd. hence alternating betwee the two on the board
            model.setPieceAt(0, col, piece);
        }
        //should not draw
        assertFalse(model.draw(),"no  draw as col 1 is not full");
    }

    @Test
    void dropPiece() {
        ConnectFourModel model = new ConnectFourModel();
        for (int col = 0; col < ConnectFourModel.COLUMNS; col++) {
            Piece piece = new RedCircle(5,col);
            model.setPieceAt(5,col,piece); //fill up bottom row
        }
        assertEquals(4,model.dropPiece(1)); // should be dropped to row 4 as row 5 is full

    }
    @Test
    void notDropPiece() {
        ConnectFourModel model = new ConnectFourModel();
        for (int row = 0; row < ConnectFourModel.ROWS; row++) {
            for (int col = 0; col < ConnectFourModel.COLUMNS; col++) {
                Piece piece = new RedCircle(row,col); // just fill up the board
                model.setPieceAt(row,col,piece);
            }
        }assertEquals(-1,model.dropPiece(1));//should not dop piece in a full board
    }
}