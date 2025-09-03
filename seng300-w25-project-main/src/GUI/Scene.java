package GUI;

import match_making.Game;
import networking.Client;
import networking.server_events.MatchFoundEvent;
import networking.server_events.ServerEventListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Used for methods common to some or all scenes.
 */
public abstract class Scene extends JPanel {
    public static final String IMAGES_PATH = "resources/images/";
    public static final String UI_ICON_SETTINGS = "ui_icons/SettingButton.png";
    public static final String UI_ICON_SOCIAL = "ui_icons/SocialButton.png";
    public static final String UI_ICON_PROFILE = "ui_icons/ProfileButton.png";
    public static final String UI_ICON_GAME_LIBRARY = "ui_icons/GameLibraryButton.png";

    public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public JPanel buttonPanel = new JPanel();
    public JButton libraryButton = new JButton();
    public JButton homepageButton = new JButton();
    public JButton socialButton = new JButton();
    public JButton settingsButton = new JButton();

    protected final Client client;
    protected final App app;

    public Scene(App app) {
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
                assignImages();
            }
        });
        setLayout(new GridLayout(1, 1));
        this.app = app;
        this.client = app.getClient();
    }

    /**
     * Create a button with a set size.
     * @param name button name.
     * @param width button width.
     * @param height button height.
     * @return the new button.
     */
    public JButton createButton(String name, int width, int height) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(width, height));
        button.setName(name);
        button.setFocusable(false);
        button.setBackground(CustomColor.DARK_BLUE_1);

        return button;
    }

    /**
     * Create a button without a set size.
     * @param name button name.
     * @return the new button.
     */
    public JButton createButton(String name) {
        JButton button = new JButton();
        button.setName(name);
        button.setFocusable(false);
        button.setBackground(CustomColor.DARK_BLUE_1);

        return button;
    }

    /**
     * Initializes menu buttons related to scene switching used in game library and profile scenes.
     */
    public void initializeMenuButtons() {
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CustomColor.BLUE_2);

        int buttonSize = (int) (this.getHeight() * 0.0925);

        libraryButton = createButton(App.GAME_LIBRARY, buttonSize, buttonSize);
        homepageButton = createButton(App.PROFILE_HOME, buttonSize, buttonSize);
        socialButton = createButton(App.PROFILE_SOCIAL, buttonSize, buttonSize);
        settingsButton = createButton(App.PROFILE_SETTINGS, buttonSize, buttonSize);

        buttonPanel.add(libraryButton);
        buttonPanel.add(homepageButton);
        buttonPanel.add(socialButton);
        buttonPanel.add(settingsButton);
    }

    /**
     * Initializes menu button listeners related to scene switching used in game library and profile scenes.
     */
    public void setupMenuButtonListeners() {
        libraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateScene();
                showScene(App.GAME_LIBRARY);
            }
        });

        homepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateScene();
                showScene(App.PROFILE_HOME);
            }
        });

        socialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateScene();
                showScene(App.PROFILE_SOCIAL);
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateScene();
                showScene(App.PROFILE_SETTINGS);

            }
        });
    }

    /**
     * Adds Images to the menu buttons related to scene switching used in game library and profile scenes.
     */
    public void addMenuButtonImages() {
        settingsButton.setIcon(getButtonImage(UI_ICON_SETTINGS, settingsButton.getWidth(), settingsButton.getHeight()));
        socialButton.setIcon(getButtonImage(UI_ICON_SOCIAL, socialButton.getWidth(), socialButton.getHeight()));
        homepageButton.setIcon(getButtonImage(UI_ICON_PROFILE, homepageButton.getWidth(), homepageButton.getHeight()));
        libraryButton.setIcon(getButtonImage(UI_ICON_GAME_LIBRARY, libraryButton.getWidth(), libraryButton.getHeight()));
    }

    /**
     * Gets an image from the images folder which can then be added to components.
     * @param fileName name of the image.
     * @param width width to size image to.
     * @param height height to size image to.
     * @return the resized image.
     */
    public ImageIcon getButtonImage(String fileName, int width, int height) {
        ImageIcon original = new ImageIcon(IMAGES_PATH + fileName);
        ImageIcon resized = new ImageIcon(original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return resized;
    }

    /**
     * Replaces built in scrollbars with custom one.
     */
    public void editUIScrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(32);

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = CustomColor.DARK_BLUE_1; // the bar that moves
                this.trackColor = CustomColor.BLUE_1; // the track for the bar
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                // Remove the bottom button of scroll bar
                return setButtonSizeZero();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                // Remove the top button of scroll bar
                return setButtonSizeZero();
            }

            private JButton setButtonSizeZero() {
                // Set the button size to 0 so it essentially dne
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
    }

    /**
     * Get access to the top level of the application.
     * @return a reference to the top level of the application.
     */
    protected App getApp() {
        return app;
    }

    /**
     * Gets the size of the top level of the application.
     * @return a Dimension object which is the size of the top level of the application.
     */
    protected Dimension getAppSize() {
        return getApp().getSize();
    }

    /**
     * Call this method to show/swap scenes.
     * <br>
     * This method should be used in some action listener. E.g., for a button or keyboard input.
     * @param title the title of the scene to swap to
     *              <br> (refer to constant Strings declared in the top level of the application)
     */
    protected void showScene(String title) {
        getApp().showScene(title);
    }

//    protected void removeScene(String title) {
//        getApp().
//    }

    private static int COUNT = 0;

    /**
     * Call this method to launch a leaderBoard.
     * @param game the game to launch
     */
    protected void launchLeaderboard(String game) {
        getApp().add(new LeaderboardScene(app, game), "L"+game+COUNT);
        showScene("L"+game+COUNT);
    }

    /**
     * Method for initializing the components of a scene.
     */
    protected abstract void initializeComponents();

    /**
     * Method for resizing the components for a scene.
     */
    protected abstract void resizeComponents();

    /**
     * Used to update the scene.
     */
    protected abstract void updateScene(); // TODO Bug where it only updates the scene when you click off the scene

    /**
     * Method for initializing the listeners for a scene.
     */
    protected void setupListeners() {
        client.addServerEventListener(new ServerEventListener() {
            @Override
            public void matchFound(MatchFoundEvent e) {
                var scene = ListOfGames.getGame(getApp(), e.getGame().name());
                scene.initBoard(e.getGameState().board(), e.getGameState().players().current().getPlayerID());
                showScene(App.GAME_SCENE);
            }
        });
    }

    /**
     * Method for assigning images.
     */
    protected abstract void assignImages();
}
