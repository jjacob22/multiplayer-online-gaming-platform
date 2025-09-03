package GUI.Games;

import GUI.App;
import GUI.CustomColor;
import GUI.RoundButton;
import game.GameStatus;
import game.Piece;
import game.connect_four.ConnectFourPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ConnectFour extends GameScene {

    private JPanel connectFourPanel;
    private JPanel roundBorder;
    private JPanel connectFourBoard;
    private final int BOARD_WIDTH = 7;
    private final int BOARD_HEIGHT = 6;
    private final int SQUARE_SIZE;
    private final ImageIcon[] SPRITES;
    private RoundButton[][] grid = new RoundButton[BOARD_HEIGHT][BOARD_WIDTH];

    public ConnectFour(App app) {
        super(app);
        nimbusLookAndFeel();

        SQUARE_SIZE = this.getHeight()/(BOARD_HEIGHT + 1);
        SPRITES = initSprites();

        connectFourPanel = new JPanel(new BorderLayout());
        connectFourBoard = new JPanel(new GridLayout(BOARD_HEIGHT, BOARD_WIDTH, 5, 5));
        roundBorder = new JPanel(new GridLayout(1, 1)) {
            // Square magic
            @Override
            public void doLayout() {
                int size = Math.min(getWidth(), getHeight());
                setSize(size, size * 6/7);
                setLocation((getParent().getWidth() - size) / 2, (getParent().getHeight() - (size * 6/7)) / 2);
                super.doLayout();
            }
        };

        connectFourPanel.setBackground(CustomColor.BLUE_2);
        connectFourPanel.setBorder(BorderFactory.createLineBorder(connectFourPanel.getBackground(), SIDE_BORDER));

        connectFourBoard.setBackground(CustomColor.DARK_BLUE_1);

        roundBorder.setBorder(BorderFactory.createLineBorder(CustomColor.DARK_BLUE_1, 15, true));
        roundBorder.setBackground(null);

        initGrid();
        roundBorder.add(connectFourBoard);
        connectFourPanel.add(roundBorder, BorderLayout.CENTER);
        game.add(connectFourPanel, BorderLayout.CENTER);

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
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                grid[i][j] = new RoundButton();
                int finalI = i; int finalJ = j;
                //grid[i][j].addActionListener(e -> pieceClick(finalI, finalJ));
                // TODO show where piece would go when hovering over a spot

                grid[i][j].addMouseListener(new MouseListener() {
                    int changedI = -1;
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        pieceClick(finalI, finalJ);
                        changedI = -1;
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
                            if (grid[i][finalJ].getSprite() == null) {
                                grid[i][finalJ].setSprite(teamPiece());
                                changedI = i;
                                break;
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (changedI != -1) {
                            grid[changedI][finalJ].setSprite(null);
                        }
                    }
                });

                connectFourBoard.add(grid[i][j]);
            }
        }
    }

    private ImageIcon teamPiece() {
        if (client.getMyID() != getPlayer1()) {
            return SPRITES[0];
        } else {
            return SPRITES[1];
        }
    }

    private void pieceClick(int i, int j) {
        client.placePiece(i, j);
    }

    private ImageIcon[] initSprites() {
        String[] fileNames = {
                "piece1", "piece2"
        };
        ImageIcon[] sprites = new ImageIcon[2];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new ImageIcon(new ImageIcon("resources/images/game_pieces/connect_four/" + fileNames[i] + ".png").getImage().getScaledInstance(SQUARE_SIZE - 15, SQUARE_SIZE - 15, Image.SCALE_SMOOTH));
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
                        case ConnectFourPiece.HOLLOW_CIRCLE:    // Team 1
                            grid[i][j].setSprite(SPRITES[0]);
                            break;
                        case ConnectFourPiece.SOLID_CIRCLE:     // Team 2
                            grid[i][j].setSprite(SPRITES[1]);
                            break;
                    }
                }
            }
        }
    }
}
