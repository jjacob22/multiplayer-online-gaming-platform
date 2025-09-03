package GUI;

import match_making.Game;
import networking.Client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Displays UI components for selecting finding a player.
 */
public class PlayerSearchScene extends Scene {
    public static final String IMG_NAME_PROFILE = "ui_icons/ProfileButton.png";
    public static final String IMG_NAME_FRIEND = "ui_icons/ProfileSearchPlayerButton.png";
    public static final String IMG_NAME_CHALLENGE = "ui_icons/ChallengePlayerButton.png";

    private final JPanel playersPanel = new JPanel();
    private final JTextField playerSearchField = new JTextField();
    private JScrollPane playersScrollPane = new JScrollPane();

    private JFrame playerSearchFrame;

    public PlayerSearchScene(JFrame frame, App app) {
        super(app);
        playerSearchFrame = frame;
        playerSearchFrame.add(this);

        initializeComponents();
        setupListeners();
    }

    /**
     * Initializes the base information of components.
     */
    @Override
    protected void initializeComponents() {
        setBackground(CustomColor.BLUE_2);
        setLayout(new BorderLayout());

        playersPanel.setBackground(CustomColor.BLUE_1);
        playersPanel.setLayout(new GridBagLayout());

        playersScrollPane = new JScrollPane(playersPanel);
        playersScrollPane.setBackground(CustomColor.BLUE_2);
        playersScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        playersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        playersScrollPane.setBorder(new BevelBorder(1, CustomColor.DARK_BLUE_2, CustomColor.DARK_BLUE_1));
        editUIScrollPane(playersScrollPane);

        playerSearchField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.028)));
        playerSearchField.setText("search");
        playerSearchField.setBackground(CustomColor.WHITE_1);
        playerSearchField.setForeground(CustomColor.GRAY);

        createPlayerInfoPanels();

        add(playerSearchField, BorderLayout.NORTH);
        add(playersScrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates a panel for each player.
     */
    public void createPlayerInfoPanels() {
        int i = 0;

        for (String username : client.getOnlinePLayers()) {
            JPanel playerInfoPanel = new JPanel();
            playerInfoPanel.setLayout(new GridBagLayout());
            playerInfoPanel.setBackground(CustomColor.BLUE_1);
            playerInfoPanel.setBorder(BorderFactory.createEmptyBorder(1,0, 1, 0));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 0;

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0;

            int buttonSize = (int) (playerSearchFrame.getWidth() * 0.10);

            JButton playerAvatar = new JButton();
            playerAvatar.setPreferredSize(new Dimension(buttonSize, buttonSize));
            playerAvatar.setIcon(getButtonImage(IMG_NAME_PROFILE, buttonSize, buttonSize));
            playerInfoPanel.add(playerAvatar, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;

            JTextField usernameField = new JTextField();
            usernameField.setText(username);
            usernameField.setName(username);
            usernameField.setFocusable(false);
            usernameField.setBackground(CustomColor.BLUE_2);
            usernameField.setForeground(CustomColor.GREEN_2);
            usernameField.setHorizontalAlignment(JTextField.CENTER);
            usernameField.setBorder(null);
            usernameField.setFont(new Font("", Font.PLAIN, (int) (playerSearchFrame.getHeight() * 0.07)));
            playerInfoPanel.add(usernameField, gbc);

            gbc.gridx = 2;
            gbc.weightx = 0;

            JButton sendFriendRequestButton = new JButton();
            sendFriendRequestButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
            sendFriendRequestButton.setIcon(getButtonImage(IMG_NAME_FRIEND, buttonSize, buttonSize));
            sendFriendRequestButton.setName("FriendRequestButton");
            playerInfoPanel.add(sendFriendRequestButton, gbc);

            sendFriendRequestButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.sendFriendRequest(usernameField.getName()); // TODO database error
                }
            });

            gbc.gridx = 3;
            gbc.weightx = 0;

            JButton sendChallengeRequestButton = new JButton();
            sendChallengeRequestButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
            sendChallengeRequestButton.setIcon(getButtonImage(IMG_NAME_CHALLENGE, buttonSize, buttonSize));
            sendChallengeRequestButton.setName("ChallengeButton");
            playerInfoPanel.add(sendChallengeRequestButton, gbc);

            sendChallengeRequestButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gameSelectChallengePopup(username);
                }
            });

            gbc.gridy = i;
            gbc.weightx = 1;
            gbc.weighty = 0;

            playersPanel.add(playerInfoPanel, gbc);
            i++;
        }
    }

    /**
     * Resize components not already resized by a layout.
     */
    @Override
    protected void resizeComponents() {
    }

    @Override
    protected void updateScene() {

    }

    /**
     * Adds specific listeners to components.
     */
    @Override
    protected void setupListeners() {
        super.setupListeners();
        playerSearchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (playerSearchField.getText().equals("search")) {
                    playerSearchField.setText("");
                    playerSearchField.setForeground(CustomColor.GREEN_2);
                }
            }
        });

        playerSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Check if the pressed key is the Enter key
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    findPlayerSearchResults();
                }
            }
        });
    }

    private void gameSelectChallengePopup(String username) {
        JDialog challengePopup = new JDialog();
        challengePopup.setBounds(this.getWidth(),this.getHeight(), this.getWidth(), this.getHeight());
        challengePopup.setLayout(new FlowLayout());
        challengePopup.setResizable(false);

        JButton chessButton = new JButton(Game.Chess.toString());
        JButton connect4Button = new JButton(Game.ConnectFour.toString());
        JButton ticTacToeButton = new JButton(Game.TicTacToe.toString());

        String finalUsername = username;
        chessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerSearchFrame.dispose();
                challengePopup.dispose();
                client.registerChallenge(Game.Chess, finalUsername);
            }
        });

        connect4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerSearchFrame.dispose();
                challengePopup.dispose();
                client.registerChallenge(Game.ConnectFour, finalUsername);
            }
        });

        ticTacToeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerSearchFrame.dispose();
                challengePopup.dispose();
                client.registerChallenge(Game.TicTacToe, finalUsername);
            }
        });

        Dimension buttonSize = new Dimension((int) (this.getWidth() * 0.30), (int) (this.getHeight() * 0.30));
        chessButton.setPreferredSize(buttonSize);
        connect4Button.setPreferredSize(buttonSize);
        ticTacToeButton.setPreferredSize(buttonSize);

        chessButton.setBackground(CustomColor.GREEN_2);
        chessButton.setForeground(CustomColor.DARK_BLUE_1);

        connect4Button.setBackground(CustomColor.GREEN_2);
        connect4Button.setForeground(CustomColor.DARK_BLUE_1);

        ticTacToeButton.setBackground(CustomColor.GREEN_2);
        ticTacToeButton.setForeground(CustomColor.DARK_BLUE_1);

        challengePopup.add(chessButton);
        challengePopup.add(connect4Button);
        challengePopup.add(ticTacToeButton);

        challengePopup.setVisible(true);
        //TODO make better.
    }

    @Override
    protected void assignImages() {

    }

    /**
     * Displays the search results for a specific player.
     */
    private void findPlayerSearchResults() {
        //TODO search currently doesn't work
        /*
        String enteredText = playerSearchField.getText();

        playersPanel.removeAll();
        playersPanel.revalidate();
        playersPanel.repaint();

        ArrayList<String> visiblePlayers = new ArrayList<>();

        for (String player : ) {
            if (player.toLowerCase().contains(enteredText.toLowerCase().replace(" ", ""))) {
                visiblePlayers.add(player);
            }
        }

        createPlayerInfoPanels(visiblePlayers);

         */
    }
}