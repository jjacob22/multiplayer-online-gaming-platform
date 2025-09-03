package GUI;

import networking.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameOverScene extends Scene{
    private static final String VICTORY_BG = "resources/images/backgrounds/VictoryBG.png";
    private static final String LOSS_BG = "resources/images/backgrounds/LossBG.png";

    private final JLabel gameEndBackground = new JLabel();
    private final JPanel gameOverPanel = new JPanel();
    private final JButton continueButton = new JButton();
    private final boolean victory;


    public GameOverScene(App app, boolean victory) {
        super(app);

        this.victory = victory;

        initializeComponents();
        setupListeners();
    }

    /**
     * Method for initializing the components of a scene.
     * <p>
     *
     * </p>
     * Some of the scenes had this method, and it seemed like a nice way to split up some of the code so it is more readable.
     */
    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(CustomColor.BLUE_2);

        continueButton.setName("Continue");
        continueButton.setText("Continue");
        continueButton.setBackground(CustomColor.GREEN_2);

        gameOverPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        gameOverPanel.add(continueButton);

        gameEndBackground.setLayout(new GridLayout(1, 1));

        add(gameEndBackground, BorderLayout.CENTER);
        add(gameOverPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void resizeComponents() {

    }

    @Override
    protected void updateScene() {

    }

    @Override
    protected void setupListeners() {
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showScene(App.GAME_LIBRARY);
//                getApp().
            }
        });
    }

    @Override
    protected void assignImages() {
        ImageIcon background;
        if (victory) {
            background = new ImageIcon(VICTORY_BG);
        } else {
            background = new ImageIcon(LOSS_BG);
        }
        background = new ImageIcon(background.getImage().getScaledInstance(app.getWidth(), app.getHeight(), Image.SCALE_SMOOTH));
        gameEndBackground.setIcon(background);
    }
}
