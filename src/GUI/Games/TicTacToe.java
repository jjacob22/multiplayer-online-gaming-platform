package GUI.Games;

import GUI.App;
import GUI.CustomButton;
import GUI.CustomColor;
import game.GameStatus;
import game.Piece;
import game.tic_tac_toe.TicTacToePiece;
import match_making.Game;
import networking.Client;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class TicTacToe extends GameScene {

    private JPanel ticTacToePanel;
    private JPanel roundBorder;
    private JPanel ticTacToeBoard;
    private final int BOARD_LENGTH = 3;
    private final int SQUARE_SIZE;
    private final ImageIcon[] SPRITES;
    private CustomButton[][] grid = new CustomButton[BOARD_LENGTH][BOARD_LENGTH];

    public TicTacToe(App app) {
        super(app);
        nimbusLookAndFeel();

        SQUARE_SIZE = this.getHeight()/(BOARD_LENGTH + 1);
        SPRITES = initSprites();

        ticTacToePanel = new JPanel(new BorderLayout());
        ticTacToeBoard = new JPanel(new GridLayout(BOARD_LENGTH, BOARD_LENGTH, 10, 10));
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

        ticTacToePanel.setBackground(CustomColor.BLUE_2);
        ticTacToePanel.setBorder(BorderFactory.createLineBorder(ticTacToePanel.getBackground(), SIDE_BORDER));

        ticTacToeBoard.setBackground(CustomColor.DARK_BLUE_1);

        roundBorder.setBorder(BorderFactory.createLineBorder(CustomColor.DARK_BLUE_1, 5, true));
        roundBorder.setBackground(null);

        initGrid();
        roundBorder.add(ticTacToeBoard);
        ticTacToePanel.add(roundBorder, BorderLayout.CENTER);
        game.add(ticTacToePanel, BorderLayout.CENTER);

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

    private void initGrid() {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                grid[i][j] = new CustomButton();
                grid[i][j].setBackground(CustomColor.WHITE_2);
                int finalI = i; int finalJ = j;
                grid[i][j].addActionListener(e -> pieceClick(finalI, finalJ));
                ticTacToeBoard.add(grid[i][j]);
            }
        }
    }

    private void pieceClick(int i, int j) {
        client.placePiece(i, j);
    }

    private ImageIcon[] initSprites() {
        String[] fileNames = {
                "star", "shell"
        };
        ImageIcon[] sprites = new ImageIcon[2];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new ImageIcon(new ImageIcon("resources/images/game_pieces/tic_tac_toe/" + fileNames[i] + ".png").getImage().getScaledInstance(SQUARE_SIZE - 15, SQUARE_SIZE - 15, Image.SCALE_SMOOTH));
        }
        return sprites;
    }

    public void updateBoard(Piece[][] board) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (board[i][j] == null) {
                    grid[i][j].setSprite(null);
                } else {
                    switch (board[i][j].getSymbol()) {
                        case TicTacToePiece.SYMBOL_X:
                            grid[i][j].setSprite(SPRITES[0]);
                            break;
                        case TicTacToePiece.SYMBOL_O:
                            grid[i][j].setSprite(SPRITES[1]);
                            break;
                    }
                }
            }
        }
    }
}