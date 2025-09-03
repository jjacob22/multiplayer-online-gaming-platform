package GUI.Games;

import GUI.App;
import GUI.CustomButton;
import GUI.CustomColor;
import GUI.ListOfGames;
import game.GameStatus;
import game.Piece;
import game.chess.ChessPiece;
import networking.Client;
import networking.requestMessages.PromotePawn;
import networking.server_events.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class Chess extends GameScene {
    private JPanel chessPanel;
    private JPanel chessBoard;
    private JPanel roundBorder;
    private final int BOARD_LENGTH = 8;
    private final int SQUARE_SIZE;
    private CustomButton[][] grid = new CustomButton[BOARD_LENGTH][BOARD_LENGTH];
    private final ImageIcon[] SPRITES;
    private boolean pieceTapped = false;
    private int[] tappedXY;
    private int[] startHoldXY;
    private int[] endHoldXY;
    private boolean pieceHeld = false;
    private ImageIcon newImageIcon;
    private ImageIcon oldImageIcon;
    /*
    -1 = EMPTY
    0 = W_Pawn | 1 = W_Rook | 2 = W_Horse | 3 = W_Bishop | 4 = W_Queen | 5 = W_King
    6 = B_Pawn | 7 = B_Rook | 8 = B_Horse | 9 = B_Bishop | 10 = B_Queen | 11 = B_King
     */

    public Chess(App app) {
        super(app);
        nimbusLookAndFeel();

        SQUARE_SIZE = this.getHeight()/(BOARD_LENGTH + 1);
        SPRITES = initSprites();

        chessPanel = new JPanel(new BorderLayout());
        chessBoard = new JPanel(new GridLayout(BOARD_LENGTH, BOARD_LENGTH));
        roundBorder = new JPanel(new GridLayout(1, 1)) {
            // Square magic
            @Override
            public void doLayout() {
                int size = Math.min(getWidth(), getHeight());
                setSize(size, size);
                setLocation((getParent().getWidth() - size) / 2, (getParent().getHeight() - size) / 2);
                super.doLayout();
            }
        };

        chessPanel.setBackground(CustomColor.BLUE_2);
        chessPanel.setBorder(BorderFactory.createLineBorder(chessPanel.getBackground(), SIDE_BORDER));

        chessBoard.setBackground(CustomColor.DARK_BLUE_1);

        roundBorder.setBorder(BorderFactory.createLineBorder(CustomColor.DARK_BLUE_1, 15, true));
        roundBorder.setBackground(null);

        initGrid();

        roundBorder.add(chessBoard);
        chessPanel.add(roundBorder, BorderLayout.CENTER);
        game.add(chessPanel, BorderLayout.CENTER);

        defaultLookAndFeel();
    }

    @Override
    protected void resizeComponents() {

    }

    @Override
    protected void updateScene() {

    }

    @Override
    protected void assignImages() {

    }

    private void promotePawnScreen() {
        JOptionPane optionPane = new JOptionPane();
        optionPane.setFocusable(false);

        int newPiece;

        if (getPlayer1() == client.getMyID()) {
            newPiece = optionPane.showOptionDialog(roundBorder, null, "Pawn Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, Arrays.copyOfRange(SPRITES, 1, 5), null);

        } else {
            newPiece = optionPane.showOptionDialog(roundBorder, null, "Pawn Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, Arrays.copyOfRange(SPRITES, 7, 12), null);
        }

        switch (newPiece) {
            case 0:
                client.promotePawn(PromotePawn.Promotion.Rook);
                break;
            case 1:
                client.promotePawn(PromotePawn.Promotion.Knight);
                break;
            case 2:
                client.promotePawn(PromotePawn.Promotion.Bishop);
                break;
            case 3:
                client.promotePawn(PromotePawn.Promotion.Queen);
                break;
        }
    }

    private Piece[][] flipGrid(Piece[][] board) {
        Piece[][] flippedGrid = new Piece[BOARD_LENGTH][BOARD_LENGTH];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                flippedGrid[flipCoordinate(i)][flipCoordinate(j)] = board[i][j];
            }
        }
        return flippedGrid;
    }

    /**
     * Used to flip coordinates when the board is flipped
     * (black pieces on bottom)
     * @param i the coordinate to flip
     * @return the flipped coordinate
     */
    private int flipCoordinate(int i) {
        return BOARD_LENGTH - i - 1;
    }

    /**
     * Initialize grid of buttons
     */
    private void initGrid() {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                grid[i][j] = new CustomButton();
                if ((i + j) % 2 == 0) grid[i][j].setBackground(CustomColor.WHITE_2);
                else grid[i][j].setBackground(CustomColor.BLUE_2);
                int finalI = i; int finalJ = j;
                grid[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        pieceClick(finalI, finalJ);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (isOwnPiece(finalI, finalJ)) {
                            newImageIcon = grid[finalI][finalJ].getSprite();
                            if (newImageIcon != null) {
                                startHoldXY = new int[]{finalI, finalJ};
                                pieceHeld = true;
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (pieceHeld && (startHoldXY[0] != endHoldXY[0] || startHoldXY[1] != endHoldXY[1])) {
                            System.out.println("Start I: " + startHoldXY[0] + " Start J: " + startHoldXY[1] + " End I: " + endHoldXY[0] + " End J: " + endHoldXY[1]);
                            movePiece(startHoldXY[0], startHoldXY[1], endHoldXY[0], endHoldXY[1]);
                        }
                        pieceHeld = false;
                        oldImageIcon = null;
                        newImageIcon = null;
                        startHoldXY = null;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (pieceHeld) {
                            oldImageIcon = grid[finalI][finalJ].getSprite();
                            grid[finalI][finalJ].setSprite(newImageIcon);
                        }
                        endHoldXY = new int[]{finalI, finalJ};
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (pieceHeld) {
                            if (pieceTapped) resetPieceTapped();
                            grid[finalI][finalJ].setSprite(oldImageIcon);
                        }
                    }
                });
                chessBoard.add(grid[i][j]);
            }
        }
    }

    private void movePiece(int startI, int startJ, int endI, int endJ) {
        if (getPlayer1() == client.getMyID()) {
            client.movePiece(startI, startJ, endI, endJ);
        } else {
            client.movePiece(flipCoordinate(startI), flipCoordinate(startJ), flipCoordinate(endI), flipCoordinate(endJ));
        }

        pawnCheck(endI, endJ);
    }

    private void pawnCheck(int i, int j) {
        if (i == 0 || i == BOARD_LENGTH - 1) {
            if (grid[i][j].getSprite() == SPRITES[0] || grid[i][j].getSprite() == SPRITES[6]) {
                promotePawnScreen();
            }
        }
    }

    /**
     * moves pieces
     * selectedXY is startXY
     * i and j is endXY
     * @param i Y location of click
     * @param j X location of click
     */
    private void pieceClick(int i, int j) {
        if (pieceTapped) {
            if (i != tappedXY[0] || j != tappedXY[1]) {
                movePiece(tappedXY[0], tappedXY[1], i, j);
            }
            resetPieceTapped();
        } else {

            if (grid[i][j].getSprite() != null && isOwnPiece(i, j)) {
                tappedXY = new int[]{i, j};
                grid[i][j].setBackground(CustomColor.BLUE_1);
                pieceTapped = true;
            }
        }
    }

    private boolean isOwnPiece(int i, int j) {
        if (client.getMyID() == getPlayer1()) {
            // White pieces from 0 -> 5
            for (int k = 0; k <= 5; k++) {
                if (grid[i][j].getSprite() == SPRITES[k]) {
                    return true;
                }
            }
        } else {
            // Black pieces from 6 -> 11
            for (int k = 6; k <= 11; k++) {
                if (grid[i][j].getSprite() == SPRITES[k]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Resets the piece currently tapped
     */
    private void resetPieceTapped() {
        if ((tappedXY[0] + tappedXY[1]) % 2 == 0) grid[tappedXY[0]][tappedXY[1]].setBackground(CustomColor.WHITE_2);
        else grid[tappedXY[0]][tappedXY[1]].setBackground(CustomColor.BLUE_2);
        pieceTapped = false;
    }

    /**
     * Initialize sprites
     * @return all the chess sprites
     */
    private ImageIcon[] initSprites() {
        String[] fileNames = {
                "WhitePawn", "WhiteRook", "WhiteHorse", "WhiteBishop", "WhiteQueen","WhiteKing",
                "BlackPawn", "BlackRook", "BlackHorse", "BlackBishop", "BlackQueen","BlackKing"
        };
        ImageIcon[] sprites = new ImageIcon[12];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new ImageIcon(new ImageIcon("resources/images/game_pieces/chess/" + fileNames[i] + ".png").getImage().getScaledInstance(SQUARE_SIZE - 15, SQUARE_SIZE - 15, Image.SCALE_SMOOTH));
        }
        return sprites;
    }

    public void updateBoard(Piece[][] board) {
        if (getPlayer1() != client.getMyID()) board = flipGrid(board);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (board[i][j] == null) {
                    grid[i][j].setSprite(null);
                } else {
                    switch (board[i][j].getSymbol()) {
                        case ChessPiece.PAWN_SOLID:
                            grid[i][j].setSprite(SPRITES[0]);
                            break;
                        case ChessPiece.ROOK_SOLID:
                            grid[i][j].setSprite(SPRITES[1]);
                            break;
                        case ChessPiece.KNIGHT_SOLID:
                            grid[i][j].setSprite(SPRITES[2]);
                            break;
                        case ChessPiece.BISHOP_SOLID:
                            grid[i][j].setSprite(SPRITES[3]);
                            break;
                        case ChessPiece.QUEEN_SOLID:
                            grid[i][j].setSprite(SPRITES[4]);
                            break;
                        case ChessPiece.KING_SOLID:
                            grid[i][j].setSprite(SPRITES[5]);
                            break;
                        case ChessPiece.PAWN_HOLLOW:
                            grid[i][j].setSprite(SPRITES[6]);
                            break;
                        case ChessPiece.ROOK_HOLLOW:
                            grid[i][j].setSprite(SPRITES[7]);
                            break;
                        case ChessPiece.KNIGHT_HOLLOW:
                            grid[i][j].setSprite(SPRITES[8]);
                            break;
                        case ChessPiece.BISHOP_HOLLOW:
                            grid[i][j].setSprite(SPRITES[9]);
                            break;
                        case ChessPiece.QUEEN_HOLLOW:
                            grid[i][j].setSprite(SPRITES[10]);
                            break;
                        case ChessPiece.KING_HOLLOW:
                            grid[i][j].setSprite(SPRITES[11]);
                            break;
                    }
                }
            }
        }
    }
}
