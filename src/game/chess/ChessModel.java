package game.chess;
import game.GameModel;
import game.Piece;
import game.chess.pieces.*;
import networking.requestMessages.PromotePawn;

import java.util.ArrayList;
import java.util.Arrays;


import static game.chess.ChessPiece.TEAM_1;
import static game.chess.ChessPiece.TEAM_2;
public class ChessModel extends GameModel {
    /**
     * In Chess, we will assume that "rows" represent the rank.
     * <p></p>
     * Indices 0-7 (from-to) in the first Piece[][] dimension are ranks 8-1 (from-to).
     */
    public static final int ROWS = 8;
    /**
     * In Chess, we will assume that "columns" represent the file.
     * <p></p>
     * Indices 0-7 (from-to) in the second Piece[][] dimension are files a-h (from-to).
     */
    public static final int COLUMNS = 8;
    public static final Class<?> PIECE_CLASS = ChessPiece.class;
    public ChessPiece[] whitePieceArray = new ChessPiece[16];
    public ChessPiece[] blackPieceArray = new ChessPiece[16];
    public ChessModel() {
        super(new ChessPiece[ROWS][COLUMNS]);
    }
    protected void initializePieces() {
        int count = 0;
        for (int col = 0; col < COLUMNS; col++) {
            // Initialize pawns for both teams at their respective starting positions
            blackPieceArray[count] = new Pawn(1, col, TEAM_2);
            setPieceAt(1, col, blackPieceArray[count]);
            whitePieceArray[count] = new Pawn(6, col, TEAM_1);
            setPieceAt(6, col, whitePieceArray[count]);
            count++;
        }
        // Initialize the Rooks for both teams
        Rook wRook = new Rook(7, 0, TEAM_1);
        whitePieceArray[count] = wRook;
        setPieceAt(7,0,wRook);
        Rook bRook = new Rook(0, 0, TEAM_2);
        blackPieceArray[count] = bRook;
        setPieceAt(0,0,bRook);
        count++;
        Rook wRook2 = new Rook(7, 7, TEAM_1);
        whitePieceArray[count] = wRook2;
        setPieceAt(7,7,wRook2);
        Rook bRook2 = new Rook(0, 7, TEAM_2);
        blackPieceArray[count] = bRook2;
        setPieceAt(0,7,bRook2);
        count++;
        // Initialize the Knight for both teams
        Knight wKnight = new Knight(7, 1, TEAM_1);
        whitePieceArray[count] = wKnight;
        setPieceAt(7,1,wKnight);
        Knight bKnight = new Knight(0, 1, TEAM_2);
        blackPieceArray[count] = bKnight;
        setPieceAt(0,1,bKnight);
        count++;
        Knight wKnight2 = new Knight(7, 6, TEAM_1);
        setPieceAt(7,6,wKnight2);
        whitePieceArray[count] = wKnight2;
        Knight bKnight2 = new Knight(0, 6, TEAM_2);
        setPieceAt(0,6,bKnight2);
        blackPieceArray[count] = bKnight2;
        count++;
        // Initialize the bishops for both teams
        Bishop wBishop = new Bishop(7, 2, TEAM_1);
        setPieceAt(7,2,wBishop);
        whitePieceArray[count] = wBishop;
        Bishop bBishop = new Bishop(0, 2, TEAM_2);
        setPieceAt(0,2,bBishop);
        blackPieceArray[count] = bBishop;
        count++;
        Bishop wBishop2 = new Bishop(7, 5, TEAM_1);
        setPieceAt(7,5,wBishop2);
        whitePieceArray[count] = wBishop2;
        Bishop bBishop2 = new Bishop(0, 5, TEAM_2);
        setPieceAt(0,5,bBishop2);
        blackPieceArray[count] = bBishop2;
        count++;
        // Initialize the Queens for both teams
        Queen wQueen = new Queen(7, 3, TEAM_1);
        setPieceAt(7,3,wQueen);
        whitePieceArray[count] = wQueen;
        Queen bQueen = new Queen(0, 3, TEAM_2);
        setPieceAt(0,3,bQueen);
        blackPieceArray[count] = bQueen;
        count++;
        // Initialize the Kings for both teams
        King wKing = new King(7, 4, TEAM_1);
        setPieceAt(7,4,wKing);
        whitePieceArray[count] = wKing;
        King bKing = new King(0, 4, TEAM_2);
        setPieceAt(0,4,bKing);
        blackPieceArray[count] = bKing;
    }
    // TODO Maybe this should be a boolean since what if a move fails?
    @Override
    public boolean movePiece(int toRow, int toCol, Piece piece) {
        ChessPiece targetPiece = (ChessPiece) getPieceAt(toRow, toCol);
        int startCol = piece.getCol();
        int startRow = piece.getRow();
        /*
        TODO: Handle movement validation depending on piece type.
        The Knight does not need an unobstructed path.
        The Pawn has a separate attack pattern from its regular movement pattern.
        Make sure to use the Pawn's isValidMovementPattern() vs isValidAttackPattern() appropriately.
        The King should not be able to make moves that will force it into check.
        We should make a separate helper method for determining if a piece can be attacked from some position.
         */
        if (piece instanceof Bishop) {
            Bishop bishop = (Bishop) piece;
            if (bishop.isValid(startRow, startCol, toRow, toCol, this, false)) {
                if (super.movePiece(toRow, toCol, piece) && checkToUndoMove(startRow, startCol, piece, targetPiece)){
                    return true;
                }
            }
        }
        else if (piece instanceof King) {
            King king = (King) piece;
            boolean check = isUnderAttackCastle(king.getRow(), king.getCol(), king.getOwnerID());
            boolean castle = Math.abs(toCol - startCol) == 2;

            if (castle) {
                long teamNum;
                int kingRow;
                if(piece.getOwnerID() == TEAM_1){
                    teamNum = TEAM_1;
                    kingRow = 7;
                }
                else{
                    teamNum = TEAM_2;
                    kingRow = 0;
                }
                if (toRow== kingRow && piece.getOwnerID() == teamNum) {
                    //Check that the king hasn't moved
                    if (!king.hasMoved) {
                        if (!check) {
                            int direction;
                            int rookCol;
                            if (toCol == 6) {
                                direction = 1;
                                rookCol = 7;
                            }
                            else{
                                direction = -1;
                                rookCol = 0;
                            }

                            if (getPieceAt(toRow, rookCol) != NO_PIECE && getPieceAt(toRow, rookCol).getOwnerID() == teamNum && getPieceAt( toRow, rookCol ) instanceof Rook) {
                                Rook rook = (Rook) getPieceAt(toRow, rookCol);
                                if (!rook.hasMoved) {
                                    //check that the squares are empty
//
                                    boolean canCastle = true;
                                    for (int i = startCol + direction; (0 < i) && (i < COLUMNS - direction); i= i + direction) {
                                        if (getPieceAt(toRow, i) != NO_PIECE || isUnderAttackCastle(toRow, i, king.getOwnerID()) || getPieceAt(toRow, i) instanceof Rook) {
                                            canCastle = false;
                                        }
                                    }

                                    if ( canCastle) {
                                        super.movePiece(toRow, toCol, king);
                                        super.movePiece(toRow, toCol - direction, rook);
                                        king.hasMoved = true;
                                        rook.hasMoved = true;
                                        return true;
                                    }
                                    else {
                                        return false;
                                    }

                                }
                                else {
                                        return false;
                                    }
                            }
                            else{
                                return false;
                            }
                        }
                        else {
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else {
                if (king.isValid(startRow, startCol, toRow, toCol, this, false)) {

                    if (super.movePiece(toRow, toCol, piece) && checkToUndoMove(startRow, startCol, piece, targetPiece)){

                        return true;
                    }
                }else {
                    return false;
                }
            }
        }
        else if (piece instanceof Knight) {
            Knight knight = (Knight) piece;
            if (knight.isValid(startRow, startCol, toRow, toCol, this, false)) {
                if (super.movePiece(toRow, toCol, piece) && checkToUndoMove(startRow, startCol, piece, targetPiece)){
                    return true;
                }
            }
        }
        else if (piece instanceof Pawn) {
            Pawn pawn = (Pawn) piece;
            if (pawn.isValid(startRow, startCol, toRow, toCol, this, false)) {
                //do the pawn move firstly so that promotion check can be executed
                boolean didMove = super.movePiece(toRow, toCol, piece);
                boolean test = checkToUndoMove(startRow, startCol, piece, targetPiece);
                // return didMove; //true if move was successful, false otherwise
                if (didMove && test&& pawn.isPromotionRow(toRow)){ //including check for promotion now that the pawn has moved. If it is in the promotion row, then set the flag for awaiting promotion
                    pawn.setAwaitingPromotion(true);  //set flag to true
                    return true; // Move successful, awaiting promotion
                }
                // else if case to keep logic as it was just in case it is not in a promotion row
                else if (didMove && test){
                    return true;   //literally same as it was before i included the pawn check :(
                }else {
                    return false;
                }
            }
        }
        else if (piece instanceof Queen) {
            Queen queen = (Queen) piece;

            if (queen.isValid(startRow, startCol, toRow, toCol, this, false)) {
                if (super.movePiece(toRow, toCol, piece) && checkToUndoMove(startRow, startCol, piece, targetPiece)){
                    return true;
                }
            }
        }
        else if (piece instanceof Rook) {
            Rook rook = (Rook) piece;

            if (rook.isValid(startRow, startCol, toRow, toCol, this, false)) {
                if (super.movePiece(toRow, toCol, piece) && checkToUndoMove(startRow, startCol, piece, targetPiece)){

                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkToUndoMove(int startRow, int startCol, Piece piece, Piece target){
        if (isKingUnderAttack(piece.getOwnerID(), this)) {
            super.movePiece(startRow, startCol ,piece);
            ChessPiece[] opponentPieces;
            if (target != NO_PIECE || target != null){
                if (TEAM_1 == target.getOwnerID()) {
                    opponentPieces =  blackPieceArray;
                }else {
                    opponentPieces = whitePieceArray;
                }
                for (int i = 0; i < opponentPieces.length; i++) {
                    if (opponentPieces[i] == NO_PIECE) {
                        opponentPieces[i] = (ChessPiece) target;
                        break;
                    }
                }
                setPieceAt(target.getRow(), target.getCol(), target);


                return false;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }
    protected boolean isKingUnderAttack(long ownerID, ChessModel game) {
        ChessPiece[] opponentPieces;
        ChessPiece[] myTeam;
        if (TEAM_1 == ownerID) {
            opponentPieces =  blackPieceArray;
            myTeam = whitePieceArray;
        }else {
            opponentPieces = whitePieceArray;
            myTeam = blackPieceArray;
        }
        int myKingRow = -1;
        int myKingCol = -1;
        for (int i = 0; i < myTeam.length; i++){
            if (myTeam[i] instanceof King){
                myKingRow = myTeam[i].getRow();
                myKingCol = myTeam[i].getCol();
            }
        }
        if (myKingRow != -1 && myKingCol != -1){
            int opponentPieceRow;
            int opponentPieceCol;
            for (int i = 0; i < opponentPieces.length; i++) {
                if (opponentPieces[i] != null || opponentPieces[i] != NO_PIECE){
                    opponentPieceRow = opponentPieces[i].getRow();
                    opponentPieceCol = opponentPieces[i].getCol();
                    if(opponentPieces[i] instanceof Bishop){
                        if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                            return true;
                        }
//                        else{
//                            return false;
//                        }
                    } else if (opponentPieces[i] instanceof King) {

                        if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                            return true;
                        }
//                        else {
//                            return false;
//                        }
                    } else if (opponentPieces[i] instanceof Knight) {
                        if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                            return true;
                        }


                    } else if (opponentPieces[i] instanceof Pawn){
                        /// ADD THIS new
                        if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                            return true;
                        }


                    } else if (opponentPieces[i] instanceof Queen) {
                        if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                            return true;
                        }
                    } else if (opponentPieces[i] instanceof Rook) {
                        if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                            return true;
                        }
                    }
                }
            }
        }else {
            return false;
        }
        return false;
    }
    //implement a  promotePawn that will handle promotion
    public void promotePawn(PromotePawn.Promotion promotion) {
        int atRow= -1;
        int atCol= -1;
        long ownerId = -1;
        if (this.getPromotingPawn()!=null){
            atRow= this.getPromotingPawn().getRow();
            atCol= this.getPromotingPawn().getCol();
            ownerId = this.getPromotingPawn().getOwnerID();
        }

        Pawn pawn= new Pawn(atRow,atCol,ownerId);
        if(pawn.isPromotionRow(atRow)){
            pawn.promote(this, atRow, atCol, promotion);//handle promotion
            // I should update the team array with a new piece at the place of the pawn
            //get the promoted piece
            ChessPiece promotedPiece = (ChessPiece) getPieceAt(atRow,atCol); //get the new piece
            //Determine array that should be updated based on the team of the pawn
            ChessPiece[] teamPieces = pawn.getOwnerID()==TEAM_1 ? whitePieceArray : blackPieceArray;
            //find the pawn's index and replace it
            for (int i = 0; i < teamPieces.length; i++) {//loop through array
                if (teamPieces[i].equals(pawn)){ //check for this pawn
                    //test is that pawn is the right pawn and not just the first pawn in the team array list, due to compare
                    teamPieces[i] = promotedPiece; //replace the pawn with the new piece
                    break;  //stop once pawn has been found and replaced
                }
            }
        }
    }
    public Pawn getPromotingPawn(){
        for (Piece[] row : board) {//loop through board and check for pawns with the awaiting promotion flag true
            for (Piece piece : row) {
                if (piece instanceof Pawn && ((Pawn) piece).isAwaitingPromotion()) {
                    return (Pawn) piece;
                }
            }
        }return null;
    }
    // check If any pawn is awaiting on a promotion at all
    public boolean isAwaitingPromotion() {
        for (Piece[] row : board) {//loop through board and check for pawns with the awaiting promotion flag true
            for (Piece piece : row) {
                if (piece instanceof Pawn && ((Pawn) piece).isAwaitingPromotion()) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isClearPathCastle(int fromRow, int fromCol, int toRow, int toCol, ChessModel game) {
        if (getPieceAt(fromRow, fromCol) instanceof Pawn){
            Pawn pawn = (Pawn) getPieceAt(fromRow, fromCol);
            if (! pawn.isFirstMove()){
                int direction;
                if (pawn.getOwnerID() == TEAM_1){
                    direction = -1;
                }else{
                    direction = 1;
                }
                if (isValidCoordinate(fromRow + direction, fromCol + 1)) {
                    if (getPieceAt(fromRow + direction, fromCol + 1) instanceof King) {
                        return true;
                    }
                }
                if (isValidCoordinate(fromRow + direction, fromCol - 1)){
                    if (getPieceAt(fromRow + direction, fromCol - 1) instanceof King) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } else if (getPieceAt(fromRow, fromCol) instanceof Knight) {
            return false;
        }else{
            int deltaRow = toRow - fromRow;
            int deltaCol = toCol - fromCol;

            int rowStep = Integer.compare(deltaRow, 0);//talk about this
            int colStep = Integer.compare(deltaCol, 0);

            boolean isDiagonal = Math.abs(deltaRow) == Math.abs(deltaCol);
            boolean isHorizontal = deltaRow == 0 && deltaCol != 0;
            boolean isVertical = deltaCol == 0 && deltaRow != 0;

            if (!(isDiagonal || isHorizontal || isVertical)) {
                return false;
            }

            int row = fromRow + rowStep;
            int col = fromCol + colStep;

            while (row != toRow || col != toCol) {//does this ever run //talk about this
                ChessPiece piece = (ChessPiece) game.getPieceAt(row, col);
                // If there is a piece, and it's not the King, the path is blocked
                if (piece != NO_PIECE && !(piece instanceof King)) {
                    return false;
                }
                row += rowStep;
                col += colStep;
            }
            return true;
        }
    }
    protected boolean isUnderAttackCastle(int row, int col, long ownerID) {
        ChessPiece[] opponentPieces;
        if (TEAM_1 == ownerID) {
            opponentPieces =  blackPieceArray;
        }else {
            opponentPieces = whitePieceArray;
        }

        for (ChessPiece piece : opponentPieces) {
            if (piece == NO_PIECE|| piece == null) {
                continue;
            }

            if (piece instanceof King) {
                continue;
            }

            if (isClearPathCastle(piece.getRow(), piece.getCol(), row, col, this)) {
                return true;
            }
        }

        return false;
    }
    public boolean amIInCheckmate(long ownerID, ChessModel game){
        ChessPiece[] opponentPieces;
        ChessPiece[] myTeam;
        int myTeamNum;
        if (TEAM_1 == ownerID) {
            opponentPieces =  blackPieceArray;
            myTeam = whitePieceArray;
            myTeamNum = 1;
        }else {
            opponentPieces = whitePieceArray;
            myTeam = blackPieceArray;
            myTeamNum = 2;
        }
        boolean myTeamHasKing = false;
        boolean opponentTeamHsKing = false;
        int myKingRow = 0;
        int myKingCol = 0;
        for (int i = 0; i < myTeam.length; i++){
            if (myTeam[i] instanceof King){
                myKingRow = myTeam[i].getRow();
                myKingCol = myTeam[i].getCol();
                myTeamHasKing = true;
            }
            if (opponentPieces[i] instanceof King){
                opponentTeamHsKing = true;
            }
        }
        if (myTeamHasKing && opponentTeamHsKing){
            boolean canKingRun = false;
            for (int i = -1; i < 1; i++){//add to row
                for (int j = -1; j < 1; j++){//add to col

                    if (!((i == 0) && (j == 0))){//skipping over where is king is already
                        if (isValidCoordinate(myKingRow + i, myKingCol + j)){//if the new row, col is on the board
                            if (! isUnderAttack(myKingRow + i, myKingCol + j, myTeamNum)) {//if new row, col is not underattack

                                return false;
                            }
                        }
                    }
                }
            }

            //king can't run away
            if (! canKingRun){

                boolean canBeTaken = false;
                ArrayList<Piece> piecesAttackingKing = new ArrayList<>();
                int countForCanBeTaken = 0;
                int opponentPieceRow = 0;
                int opponentPieceCol = 0;
                for (int i = 0; i < opponentPieces.length; i++){

                    if (opponentPieces[i] != null || opponentPieces[i] != NO_PIECE){
                        opponentPieceRow = opponentPieces[i].getRow();
                        opponentPieceCol = opponentPieces[i].getCol();
                        if (opponentPieces[i] instanceof Bishop){

                            if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                                countForCanBeTaken += 1;
                                piecesAttackingKing.add(opponentPieces[i]);
                            }

                        } else if (opponentPieces[i] instanceof King) {
                            if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                                piecesAttackingKing.add(opponentPieces[i]);
                            }

                        } else if (opponentPieces[i] instanceof Knight) {
                            if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                                piecesAttackingKing.add(opponentPieces[i]);
                            }
                        }else if (opponentPieces[i] instanceof Pawn){
                            if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)) {
                                piecesAttackingKing.add( opponentPieces[i] );
                            }
                        } else if (opponentPieces[i] instanceof Queen) {

                            if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                                countForCanBeTaken += 1;
                                piecesAttackingKing.add(opponentPieces[i]);

                            }
                        } else if (opponentPieces[i] instanceof Rook) {
                            if (opponentPieces[i].isValid(opponentPieceRow, opponentPieceCol, myKingRow, myKingCol, game, true)){
                                countForCanBeTaken += 1;
                                piecesAttackingKing.add(opponentPieces[i]);
                            }
                        }
                    }
                }
                boolean doubleCheckCanBlockAttack = true;
                boolean countVaildBlock = false;

                if (countForCanBeTaken == 1){
                    countVaildBlock = true;
                }
                if (piecesAttackingKing.size() > 1){//funny size

                    doubleCheckCanBlockAttack = false;
                }


                if (doubleCheckCanBlockAttack && countVaildBlock){
                    int deltaColToKing;
                    int deltaRowToKing;
                    for (int i = 0; i < piecesAttackingKing.size(); i++){
                        deltaColToKing = Math.abs(piecesAttackingKing.get(i).getCol() - myKingCol);
                        deltaRowToKing = Math.abs(piecesAttackingKing.get(i).getRow() - myKingRow);
                        if (deltaColToKing == 1 || deltaRowToKing == 1){
                            countVaildBlock = false;
                        }
                    }
                }

                boolean itbreak = false;
                for (int i = 0; i < myTeam.length; i++) {
                    for (int j = 0; j < piecesAttackingKing.size(); j++) {
                        if (myTeam[i] != null || myTeam[i] != NO_PIECE) {

                            if (myTeam[i].isValid( myTeam[i].getRow(), myTeam[i].getCol(), piecesAttackingKing.get( j ).getRow(), piecesAttackingKing.get( j ).getCol(), game, true )) {

                                canBeTaken = true;
                                itbreak = true;
                                break;
                            }
                        }
                    }
                    if (itbreak) {
                        break;
                    }
                }
                if(!piecesAttackingKing.isEmpty()){
                    if (piecesAttackingKing.size() == 1){
                        if (canBeTaken || (doubleCheckCanBlockAttack && countVaildBlock)){
                            return false;
                        }else {
                            return true;
                        }
                    }else {
                        return true;
                    }
                    //new
                }else{
                    return false;
                }

            }else {
                return false;
            }
        }
        if (myTeamHasKing){
            return false;
        }else {
            return true;
        }
    }

    protected boolean isDraw() {
        int whiteKingCount = 0;
        int blackKingCount = 0;

        int whiteKnights = 0;
        int blackKnights = 0;

        int whiteBishopWhite = 0;
        int whiteBishopBlack = 0;
        int blackBishopWhite = 0;
        int blackBishopBlack = 0;

        int whiteOtherPieces = 0;
        int blackOtherPieces = 0;
        for (ChessPiece piece : whitePieceArray) {
            if (piece instanceof King) {
                whiteKingCount++;
            } else if (piece instanceof Knight) {
                whiteKnights++;
            } else if (piece instanceof Bishop) {
                int color = (piece.getRow() + piece.getCol()) % 2;
                if (color == 0) {
                    whiteBishopWhite++;
                } else {
                    whiteBishopBlack++;
                }
            } else if (piece instanceof Queen || piece instanceof Rook || piece instanceof Pawn) {
                whiteOtherPieces++;
            }
        }

        for (ChessPiece piece : blackPieceArray) {
            if (piece instanceof King) {
                blackKingCount++;
            } else if (piece instanceof Knight) {
                blackKnights++;
            } else if (piece instanceof Bishop) {
                int color = (piece.getRow() + piece.getCol()) % 2;
                if (color == 0) {
                    blackBishopWhite++;
                } else {
                    blackBishopBlack++;
                }
            } else if (piece instanceof Queen || piece instanceof Rook || piece instanceof Pawn) {
                blackOtherPieces++;
            }
        }

        boolean whiteInsufficient =
                whiteKingCount == 1 &&
                        whiteOtherPieces == 0 &&
                        (
                                (whiteKnights == 0 && whiteBishopWhite == 0 && whiteBishopBlack == 0) ||

                                        (whiteKnights == 1 && whiteBishopWhite == 0 && whiteBishopBlack == 0) ||

                                        (whiteKnights == 0 && (
                                                (whiteBishopWhite == 1 && whiteBishopBlack == 0) ||
                                                        (whiteBishopBlack == 1 && whiteBishopWhite == 0)

                                        )) ||

                                        (whiteKnights == 0 && (
                                                (whiteBishopWhite >= 2 && whiteBishopBlack == 0) ||
                                                        (whiteBishopBlack >= 2 && whiteBishopWhite == 0)
                                        ))
                        );

        boolean blackInsufficient =
                blackKingCount == 1 &&
                        blackOtherPieces == 0 &&
                        (
                                (blackKnights == 0 && blackBishopWhite == 0 && blackBishopBlack == 0) ||

                                        (blackKnights == 1 && blackBishopWhite == 0 && blackBishopBlack == 0) ||

                                        (blackKnights == 0 && (
                                                (blackBishopWhite == 1 && blackBishopBlack == 0) ||
                                                        (blackBishopBlack == 1 && blackBishopWhite == 0)

                                        )) ||

                                        (blackKnights == 0 && (
                                                (blackBishopWhite >= 2 && blackBishopBlack == 0) ||
                                                        (blackBishopBlack >= 2 && blackBishopWhite == 0)
                                        ))
                        );


        ArrayList<String> moveList = GameModel.getMoveList();

        boolean whiteMadeFiftyMoves = false;
        boolean blackMadeFiftyMoves = false;


        if (moveList.size() >= 50 && whiteKingCount == 1) {
            int nonPawnMoveCount = 0;

            for (int i = 0; i < moveList.size(); i++) {
                if (i % 2 == 0) {
                    String move = moveList.get(i);

                    if (!move.contains("P") && !move.contains("x")) {
                        nonPawnMoveCount++;
                    } else {
                        nonPawnMoveCount = 0;
                    }

                    if (nonPawnMoveCount == 50) {
                        whiteMadeFiftyMoves = true;
                        break;
                    }
                }
            }
        }

        if (moveList.size() >= 50 && blackKingCount == 1) {
            int nonPawnMoveCount = 0;

            for (int i = 1; i < moveList.size(); i++) {
                if (i % 2 == 1) {
                    String move = moveList.get(i);

                    if (!move.contains("P") && !move.contains("x")) {
                        nonPawnMoveCount++;
                    } else {
                        nonPawnMoveCount = 0;
                    }

                    if (nonPawnMoveCount == 50) {
                        blackMadeFiftyMoves = true;
                        break;
                    }
                }
            }
        }
        boolean drawByThreefoldRepetition = false;

        if (blackKingCount == 1 && whiteKingCount == 1 && moveList.size() >= 6) {
            // case 1: WBWBWB
            for (int i = 0; i + 5 < moveList.size(); i += 2) {//why add 2
                String w1 = moveList.get(i);
                String b1 = moveList.get(i + 1);
                String w2 = moveList.get(i + 2);
                String b2 = moveList.get(i + 3);
                String w3 = moveList.get(i + 4);
                String b3 = moveList.get(i + 5);

                if (w1.equals(w2) && w1.equals(w3) & b1.equals(b2) && b1.equals(b3)) {
                    drawByThreefoldRepetition = true;
                    break;
                }
            }

            // case2: BWBWBW
            for (int i = 1; i + 5 < moveList.size(); i += 2) {//why add 2
                String b1 = moveList.get(i);
                String w1 = moveList.get(i + 1);
                String b2 = moveList.get(i + 2);
                String w2 = moveList.get(i + 3);
                String b3 = moveList.get(i + 4);
                String w3 = moveList.get(i + 5);

                if (b1.equals(b2) && b1.equals(b3) && w1.equals(w2) && w1.equals(w3)) {
                    drawByThreefoldRepetition = true;
                    break;
                }
            }
        }

        boolean drawByInsufficientMaterial = whiteInsufficient && blackInsufficient;
        boolean drawByFiftyMoveRule = whiteMadeFiftyMoves || blackMadeFiftyMoves;

        return drawByInsufficientMaterial || drawByFiftyMoveRule || drawByThreefoldRepetition;
    }

    protected boolean isStalemate() {
        if (isTeamInStalemate(whitePieceArray, TEAM_1)) {
            return true;
        }
        if (isTeamInStalemate(blackPieceArray, TEAM_2)) {
            return true;
        }
        return false;
    }

    private boolean isTeamInStalemate(ChessPiece[] teamPieces, long teamOwnerID) {
        int whiteOtherPieceCount = 0;
        int blackOtherPieceCount = 0;
        for (ChessPiece piece : whitePieceArray) {
            if (!(piece == NO_PIECE)) {
                if (!(piece instanceof King)){
                    whiteOtherPieceCount++;
                }
            }
        }
        for (ChessPiece piece : blackPieceArray) {
            if (!(piece == NO_PIECE)) {
                if (!(piece instanceof King)){
                    blackOtherPieceCount++;
                }
            }
        }
        if (whiteOtherPieceCount == 0 && blackOtherPieceCount == 0){
            return true;
        }
        ChessPiece king = (ChessPiece) NO_PIECE;//talk about this
        for (ChessPiece piece : teamPieces) {
            if (piece instanceof King) {
                king = piece;
                break;
            }
        }
        if (king == NO_PIECE) {
            return false;
        }
        int kingRow = king.getCol();
        int kingCol = king.getRow();
        if (isUnderAttack(kingRow, kingCol, teamOwnerID)) {
            return false;
        }
        for (ChessPiece piece : teamPieces) {
            if (piece == NO_PIECE) {
                continue;
            }
            int row = piece.getRow();
            int col = piece.getCol();
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLUMNS; c++) {
                    if (piece instanceof King && ! isUnderAttack(kingRow, kingCol, teamOwnerID)) {
                        continue;
                    }
                    if (piece.isValid(row, col, r, c, this, true)) {
                        return false;
                    }
                }
            }
        }
        return true; //no valid move found, then stalemate
    }

    /**
     * Checks if a location is under attack.
     * @param row row position
     * @param col column position
     * @param ownerID ID of team which would be defending
     * @return True, iff a piece belonging to the defending ID would be able to be attacked at that position.
     */
    protected boolean isUnderAttack(int row, int col, long ownerID) {
        ChessPiece[] opponentPieces;
        if (TEAM_1 == ownerID) {
            opponentPieces =  blackPieceArray;
        }else {
            opponentPieces = whitePieceArray;
        }
        for (ChessPiece piece : opponentPieces) {

            if (piece == NO_PIECE || piece == null) {
                continue;
            }
            if (piece instanceof King) {
//
                continue;
            }
            if (isCP(piece.getRow(), piece.getCol(), row, col, this)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCP(int fromRow, int fromCol, int toRow, int toCol, ChessModel game) {

        if (isValidCoordinate(fromRow, fromCol)) {
            if (getPieceAt(fromRow, fromCol) instanceof Pawn) {
                Pawn pawn = (Pawn) getPieceAt(fromRow, fromCol);
                    int direction;
                    if (pawn.getOwnerID() == TEAM_1) {
                        direction = -1;
                    } else {
                        direction = 1;
                    }

//
                    if (isValidCoordinate(fromRow + direction, fromCol + 1)) {
                        if (getPieceAt(fromRow + direction, fromCol + 1) instanceof King) {
                            return true;
                        }
                    }
                    if (isValidCoordinate(fromRow + direction, fromCol - 1)) {
                        if (getPieceAt(fromRow + direction, fromCol - 1) instanceof King) {
                            return true;
                        }
                    }
                    return false;
            } else if (getPieceAt(fromRow, fromCol) instanceof Knight) {
                    if(isValidCoordinate(fromRow - 2, fromCol - 1)){
                        if (getPieceAt(fromRow -2, fromCol -1) instanceof King) {
                            return true;
                        }
                    }
                    // up right
                    if(isValidCoordinate(fromRow - 2, fromCol + 1)){
                        if (getPieceAt(fromRow -2, fromCol +1) instanceof King) {
                            return true;
                        }
                    }
                    // down left
                    if(isValidCoordinate(fromRow + 2, fromCol - 1)){
                        if (getPieceAt(fromRow +2, fromCol -1) instanceof King) {
                            return true;
                        }
                    }
                    // down right
                    if(isValidCoordinate(fromRow + 2, fromCol + 1)){

                        if (getPieceAt(fromRow +2, fromCol +1) instanceof King) {
                            return true;
                        }
                    }
                    // up left
                    if(isValidCoordinate(fromRow - 1, fromCol - 2)){
                        if (getPieceAt(fromRow -1, fromCol -2) instanceof King) {
                            return true;
                        }
                    }
                    // down left
                    if(isValidCoordinate(fromRow + 1, fromCol- 2)){

                        if (getPieceAt(fromRow +1, fromCol -2) instanceof King) {
                            return true;
                        }
                    }
                    // up right
                    if(isValidCoordinate(fromRow - 1, fromCol + 2)){

                        if (getPieceAt(fromRow -1, fromCol +2) instanceof King) {
                            return true;
                        }
                    }
                    // down right
                    if(isValidCoordinate(fromRow + 1, fromCol + 2)){
                        if (getPieceAt(fromRow +1, fromCol +2) instanceof King) {
                            return true;
                        }
                    }

                return false;
            } else {
                int deltaRow = toRow - fromRow;
                int deltaCol = toCol - fromCol;
                int rowStep = 0, colStep = 0;
                if(deltaRow < 0) rowStep = -1;
                if(deltaRow > 0) rowStep = 1;
                if(deltaCol < 0) colStep = -1;
                if(deltaCol > 0) colStep = 1;


                boolean isDiagonal = false;
                if (deltaRow != 0 && deltaCol != 0) {
                    isDiagonal = Math.abs(deltaRow) == Math.abs(deltaCol);
                }

                boolean isHorizontal = deltaRow == 0 && deltaCol != 0;
                boolean isVertical = deltaCol == 0 && deltaRow != 0;


                if (!(isDiagonal || isHorizontal || isVertical)) {
                    return false;
                }

                int row = fromRow + rowStep;
                int col = fromCol + colStep;

                while (row != toRow || col != toCol) {//does this ever run //talk about this
                    ChessPiece piece = (ChessPiece) game.getPieceAt(row, col);
                    if (piece != NO_PIECE && !(piece instanceof King)) {
                        return false;
                    }
                    row += rowStep;
                    col += colStep;
                }
                return true;
            }
        }
        return false;
    }
}