package GUI;

import GUI.Games.GameScene;
import match_making.Game;
import networking.Client;

import javax.swing.*;
import java.awt.*;

import static networking.PlatformServerManager.SERVER_IP_LOCAL;
import static networking.PlatformServerManager.SERVER_PORT_DEFAULT;

/**
 * Top level of the application: OMG!
 */
public class App extends JFrame {
    /**
     * Default icon for the app.
     */
    public static ImageIcon APP_ICON = new ImageIcon("resources/images/OMG-logo.png");
    /**
     * Default title for the app.
     */
    public static String TITLE = "OMG!";

    /**
     * Not too sure what this is for...
     * TODO: figure out if this is necessary?
     */
    protected static ImageIcon CURRENT_AVATAR;

    /**
     * Title for the login scene.
     */
    public static final String LOGIN = "Login";
    /**
     * Title for the create account scene.
     */
    public static final String CREATE_ACCOUNT = "Create Account";
    /**
     * Title for the game library scene.
     */
    public static final String GAME_LIBRARY = "Game Library";
    /**
     * Title for the leaderboard scene.
     */
    public static final String LEADERBOARD = "Leaderboard";
    /**
     * Title for the profile home scene.
     */
    public static final String PROFILE_HOME = "Profile Home";
    /**
     * Title for the profile social scene.
     */
    public static final String PROFILE_SOCIAL = "Profile Social";
    /**
     * Title for the profile settings scene.
     */
    public static final String PROFILE_SETTINGS = "Profile Settings";
    /**
     *
     */
    public static final String LOADING = "Loading";
    /**
     * Title for the game over scene.
     */
    public static final String GAME_OVER = "Game Over";
    /**
     * Title for the victory scene.
     */
    public static final String VICTORY = "Victory";
    /**
     * Title for the game scene.
     */
    public static final String GAME_SCENE = "Game Scene";

    /**
     * The layout for the top level of the application.
     * Helps control being able to swap between scenes without needing to create new scenes or reload everything each time.
     */
    private final CardLayout SCENES;
    /**
     * The top level of the application's interface with the network.
     */
    private Client CLIENT;


    private Game currentGame = null;
    private GameScene currentGameScene = null;

    public App(String serverIP, int serverPort) {
        super(TITLE);
        /*
        Connecting to server.

        If you would like the server to leave you alone while testing,
        just comment out the code between these comments and the end of the constructor.

        Alternatively, run the PlatformServerManager in the networking package to run the server on your local machine.
         */
        CLIENT = null;
        while (CLIENT == null) {
            try {
                CLIENT = new Client(serverIP, serverPort);
            } catch (Exception e) {
                /*
                This if statement is quite unreadable.
                Basically, it handles asking the user if they would like to attempt to connect to the server again.
                If the user selects no, the application closes.
                If the user selects yes, the application will attempt to connect to the server again.
                 */
                if (
                        JOptionPane.showConfirmDialog(
                                this,
                                "Unable to connect to the OMG! server. Would you like to retry?",
                                "OMG! (Online Multiplayer (Board) Games!)",
                                JOptionPane.YES_NO_OPTION)
                                != JOptionPane.YES_OPTION
                ) {
                    throw new RuntimeException("User gave up on the server :(");
                };
            }
        }
        /*
        End of code for connecting to server.
         */
        /*
        Initializing graphical stuff.
         */
        SCENES = new CardLayout();
        new ListOfGames();
        initAppDefaults();
        add(new LoginScene(this), LOGIN);
        add(new CreateAccountScene(this), CREATE_ACCOUNT);
        showScene(GAME_LIBRARY);
        setVisible(true);
    }

    public App(String serverIP) {
        this(serverIP, SERVER_PORT_DEFAULT);
    }

    public App() {
        this(SERVER_IP_LOCAL, SERVER_PORT_DEFAULT);
    }

    /**
     * Gets the Client for communication with the server.
     * @return a reference to the top level of the application's connection point to the server.
     */
    public Client getClient() {
        return CLIENT;
    }

    /**
     * A method to use for swapping to other scenes in the application.
     * @param title the title of the scene to swap to <br> (refer to constant Strings)
     */
    public void showScene(String title) {
        SCENES.show(getContentPane(), title);
    }

//    public void removeScene(String title) {
//        remove(Arrays.stream(getContentPane().getComponents()).anyMatch(x -> Objects.equals(x.getName(), title)));
//        remove();
//    }
    /**
     * Initialize the default configurations for the application.
     */
    private void initAppDefaults() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setBounds(0, 0, screenSize.width, screenSize.height);
        this.setMinimumSize(new Dimension((int) (screenSize.width * 0.50), (int) (screenSize.height * 0.50)));
        this.setIconImage(APP_ICON.getImage());
        this.setBackground(CustomColor.BLUE_1);
        this.setLayout(SCENES);

        CURRENT_AVATAR = APP_ICON;
    }

    /**
     * Initialize the scenes for the application.
     */
    public void initScenes() {
        add(new GameLibraryScene(this), GAME_LIBRARY);
        add(new ProfileHomepageScene(this), PROFILE_HOME);
        add(new ProfileSocialScene(this), PROFILE_SOCIAL);
        add(new ProfileSettingsScene(this), PROFILE_SETTINGS);
        add(new GameOverScene(this, true), VICTORY);
        add(new GameOverScene(this, false), GAME_OVER);
    }
}
