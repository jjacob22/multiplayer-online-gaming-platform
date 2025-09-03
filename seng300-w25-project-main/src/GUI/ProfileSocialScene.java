package GUI;

import match_making.MatchChallenge;
import networking.Client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Displays UI components for a players social page.
 */
public class ProfileSocialScene extends Scene {
    public static final String IMG_EXTENSION = ".png";
    public static final String IMG_NAME_PROFILE = "ui_icons/ProfileButton" + IMG_EXTENSION;
    public static final String IMG_NAME_PROFILE_SEARCH = "ui_icons/ProfileSearchPlayerButton" + IMG_EXTENSION;
    public static final String IMG_NAME_ACCEPT = "ui_icons/AcceptButton" + IMG_EXTENSION;
    public static final String IMG_NAME_DECLINE = "ui_icons/DeclineButton" + IMG_EXTENSION;

    private final JPanel topPanel = new JPanel();

    private final JPanel searchPlayersPanel = new JPanel();
    private JButton searchPlayersButton = new JButton();

    private final JPanel friendsPanel = new JPanel();
    private final JTextField friendsField = new JTextField();
    private final JPanel friendsEntriesPanel = new JPanel();
    private JScrollPane friendsScrollPane = new JScrollPane();
    private final JPanel innerFriendsPanel = new JPanel();
    private final JTextField friendSearchField = new JTextField();

    private final JPanel notificationsPanel = new JPanel();

    private final JPanel challengeNotificationsPanel = new JPanel();
    private final JPanel challengeNotificationsEntriesPanel = new JPanel();
    private JScrollPane challengeNotificationsScrollPane = new JScrollPane();
    private final JTextField challengeNotificationsField = new JTextField();

    private final JPanel friendsRequestsNotificationsPanel = new JPanel();
    private final JPanel friendsRequestsNotificationsEntriesPanel = new JPanel();
    private JScrollPane friendsRequestsNotificationsScrollPane = new JScrollPane();
    private final JTextField friendsRequestsNotificationsField = new JTextField();

    private final JPanel scrollBoxsPanel = new JPanel();

    private JFrame playerSearchWindow;

    private final ArrayList<String> friends = new ArrayList<>(); // Temp

    public ProfileSocialScene(App app) {
        super(app);
        initializeComponents();
        setupListeners();
    }

    /**
     * Initializes the base information of components
     */
    @Override
    protected void initializeComponents() {
        setBackground(CustomColor.BLUE_2);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder((int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.02), (int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.02)));

        initializeMenuButtons();
        socialButton.setBorder(new LineBorder(CustomColor.DARK_BLUE_2, 3));

        friendsPanel.setLayout(new BorderLayout());

        friendsField.setText("Friends");
        friendsField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.033)));
        friendsField.setFocusable(false);
        friendsField.setBorder(null);
        friendsField.setForeground(CustomColor.GREEN_2);
        friendsField.setBackground(CustomColor.BLUE_2);

        friendsEntriesPanel.setBackground(CustomColor.BLUE_1);
        friendsEntriesPanel.setLayout(new GridBagLayout());

        friendsScrollPane = new JScrollPane(friendsEntriesPanel);
        friendsScrollPane.setBackground(CustomColor.BLUE_2);
        friendsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        friendsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        friendsScrollPane.setBorder(new BevelBorder(1, CustomColor.DARK_BLUE_2, CustomColor.DARK_BLUE_1));
        editUIScrollPane(friendsScrollPane);

        friendsPanel.add(friendSearchField, BorderLayout.NORTH);
        friendsPanel.add(friendsScrollPane, BorderLayout.CENTER);


        topPanel.setLayout(new GridLayout(0, 2));

        searchPlayersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPlayersPanel.setBackground(CustomColor.BLUE_2);

        int buttonSize = (int) (this.getHeight() * 0.0925);

        searchPlayersButton = createButton("ProfileSearchPlayerButton", buttonSize, buttonSize);

        searchPlayersPanel.add(searchPlayersButton);

        topPanel.add(searchPlayersPanel);
        topPanel.add(buttonPanel);

        friendSearchField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.028)));
        friendSearchField.setText("search");
        friendSearchField.setBackground(CustomColor.WHITE_1);
        friendSearchField.setForeground(CustomColor.GRAY);


        challengeNotificationsField.setText("Challenge Notifications");
        challengeNotificationsField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.033)));
        challengeNotificationsField.setFocusable(false);
        challengeNotificationsField.setBorder(null);
        challengeNotificationsField.setForeground(CustomColor.GREEN_2);
        challengeNotificationsField.setBackground(CustomColor.BLUE_2);

        challengeNotificationsEntriesPanel.setBackground(CustomColor.BLUE_1);
        challengeNotificationsEntriesPanel.setLayout(new GridBagLayout());

        challengeNotificationsScrollPane = new JScrollPane(challengeNotificationsEntriesPanel);
        challengeNotificationsScrollPane.setBackground(CustomColor.BLUE_2);
        challengeNotificationsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        challengeNotificationsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        challengeNotificationsScrollPane.setBorder(new BevelBorder(1, CustomColor.DARK_BLUE_2, CustomColor.DARK_BLUE_1));
        editUIScrollPane(challengeNotificationsScrollPane);

        challengeNotificationsPanel.setLayout(new BorderLayout());
        challengeNotificationsPanel.setBackground(CustomColor.BLUE_2);
        challengeNotificationsPanel.add(challengeNotificationsField, BorderLayout.NORTH);
        challengeNotificationsPanel.add(challengeNotificationsScrollPane, BorderLayout.CENTER);


        friendsRequestsNotificationsField.setText("Friend Notifications");
        friendsRequestsNotificationsField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.033)));
        friendsRequestsNotificationsField.setFocusable(false);
        friendsRequestsNotificationsField.setBorder(null);
        friendsRequestsNotificationsField.setForeground(CustomColor.GREEN_2);
        friendsRequestsNotificationsField.setBackground(CustomColor.BLUE_2);

        friendsRequestsNotificationsEntriesPanel.setBackground(CustomColor.BLUE_1);
        friendsRequestsNotificationsEntriesPanel.setLayout(new GridBagLayout());

        friendsRequestsNotificationsScrollPane = new JScrollPane(friendsRequestsNotificationsEntriesPanel);
        friendsRequestsNotificationsScrollPane.setBackground(CustomColor.BLUE_2);
        friendsRequestsNotificationsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        friendsRequestsNotificationsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        friendsRequestsNotificationsScrollPane.setBorder(new BevelBorder(1, CustomColor.DARK_BLUE_2, CustomColor.DARK_BLUE_1));
        editUIScrollPane(friendsRequestsNotificationsScrollPane);

        friendsRequestsNotificationsPanel.setLayout(new BorderLayout());
        friendsRequestsNotificationsPanel.setBackground(CustomColor.BLUE_2);
        friendsRequestsNotificationsPanel.add(friendsRequestsNotificationsField, BorderLayout.NORTH);
        friendsRequestsNotificationsPanel.add(friendsRequestsNotificationsScrollPane, BorderLayout.CENTER);

        notificationsPanel.setLayout(new GridLayout(2, 1));
        notificationsPanel.setBackground(CustomColor.BLUE_2);
        notificationsPanel.add(challengeNotificationsPanel);
        notificationsPanel.add(friendsRequestsNotificationsPanel);

        innerFriendsPanel.setLayout(new BorderLayout());
        innerFriendsPanel.add(friendsField, BorderLayout.NORTH);
        innerFriendsPanel.add(friendsPanel, BorderLayout.CENTER);

        scrollBoxsPanel.setLayout(new GridLayout(0, 2, 0, (int) (this.getWidth() * 0.005)));
        scrollBoxsPanel.add(innerFriendsPanel);
        scrollBoxsPanel.add(notificationsPanel);

        displayChallengeEntries();
        displayFriendRequestEntries();

        add(topPanel, BorderLayout.NORTH);
        add(scrollBoxsPanel, BorderLayout.CENTER);
    }

    public void displayChallengeEntries() {
        int i = 0;
        for (MatchChallenge challenge : client.getChallenges()) { //TODO Loop through challenges
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

            int buttonSize = (int) (this.getWidth() * 0.04);

            JButton playerAvatar = new JButton();
            playerAvatar.setPreferredSize(new Dimension(buttonSize, buttonSize));
            playerAvatar.setIcon(getButtonImage(IMG_NAME_PROFILE, buttonSize, buttonSize)); // TODO Integration replace ProfileButton.png with getAvatar
            playerInfoPanel.add(playerAvatar, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;

            JTextField usernameField = new JTextField();
            usernameField.setText(challenge.getChallenged()); // TODO: Integration replace with player name
            usernameField.setFocusable(false);
            usernameField.setBackground(CustomColor.BLUE_2);
            usernameField.setForeground(CustomColor.GREEN_2);
            usernameField.setHorizontalAlignment(JTextField.CENTER);
            usernameField.setBorder(null);
            usernameField.setFont(new Font("", Font.PLAIN, (int) (this.getWidth() * 0.015)));
            playerInfoPanel.add(usernameField, gbc);

            gbc.gridx = 2;
            gbc.weightx = 0;

            JButton declineChallengeButton = new JButton();
            declineChallengeButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
            declineChallengeButton.setIcon(getButtonImage(IMG_NAME_DECLINE, buttonSize, buttonSize));
            declineChallengeButton.setName("DeclineChallengeButton");
            playerInfoPanel.add(declineChallengeButton, gbc);

            declineChallengeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (playerSearchWindow != null) {
                        playerSearchWindow.dispose();
                    }

                    // TODO Decline challenge method missing in client.

                }
            });

            gbc.gridx = 3;
            gbc.weightx = 0;

            JButton acceptChallengeButton = new JButton();
            acceptChallengeButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
            acceptChallengeButton.setIcon(getButtonImage(IMG_NAME_ACCEPT, buttonSize, buttonSize));
            acceptChallengeButton.setName("AcceptChallengeButton");
            playerInfoPanel.add(acceptChallengeButton, gbc);
            acceptChallengeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (playerSearchWindow != null) {
                        playerSearchWindow.dispose();
                    }
                    client.acceptChallenge(challenge);

                }
            });

            gbc.gridy = i;
            gbc.weightx = 1;
            gbc.weighty = 0;

            challengeNotificationsEntriesPanel.add(playerInfoPanel, gbc);
            i++;
        }

    }


    private void displayFriendRequestEntries() {
        int i = 0;
        for (MatchChallenge challenge : client.getChallenges()) { //TODO Loop through challenges
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

            int buttonSize = (int) (this.getWidth() * 0.04);

            JButton playerAvatar = new JButton();
            playerAvatar.setPreferredSize(new Dimension(buttonSize, buttonSize));
            playerAvatar.setIcon(getButtonImage(IMG_NAME_PROFILE, buttonSize, buttonSize)); // TODO Integration replace ProfileButton.png with getAvatar
            playerInfoPanel.add(playerAvatar, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;

            JTextField usernameField = new JTextField();
            usernameField.setText(challenge.getChallenged()); // TODO: Integration replace with player name
            usernameField.setFocusable(false);
            usernameField.setBackground(CustomColor.BLUE_2);
            usernameField.setForeground(CustomColor.GREEN_2);
            usernameField.setHorizontalAlignment(JTextField.CENTER);
            usernameField.setBorder(null);
            usernameField.setFont(new Font("", Font.PLAIN, (int) (this.getWidth() * 0.015)));
            playerInfoPanel.add(usernameField, gbc);

            gbc.gridx = 2;
            gbc.weightx = 0;

            JButton declineFriendRequestButton = new JButton();
            declineFriendRequestButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
            declineFriendRequestButton.setIcon(getButtonImage(IMG_NAME_DECLINE, buttonSize, buttonSize));
            declineFriendRequestButton.setName("DeclineFriendRequestButton");
            playerInfoPanel.add(declineFriendRequestButton, gbc);

            declineFriendRequestButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (playerSearchWindow != null) {
                        playerSearchWindow.dispose();
                    }

                    // TODO Decline challenge method missing in client.

                }
            });

            gbc.gridx = 3;
            gbc.weightx = 0;

            JButton acceptFriendRequestButton = new JButton();
            acceptFriendRequestButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
            acceptFriendRequestButton.setIcon(getButtonImage(IMG_NAME_ACCEPT, buttonSize, buttonSize));
            acceptFriendRequestButton.setName("AcceptFriendRequestButton");
            playerInfoPanel.add(acceptFriendRequestButton, gbc);
            acceptFriendRequestButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (playerSearchWindow != null) {
                        playerSearchWindow.dispose();
                    }
                    client.acceptChallenge(challenge);

                }
            });

            gbc.gridy = i;
            gbc.weightx = 1;
            gbc.weighty = 0;

            friendsRequestsNotificationsEntriesPanel.add(playerInfoPanel, gbc);
            i++;
        }

    }

    /**
     * Assigns images to components.
     */
    @Override
    protected void assignImages() {
        try {
            addMenuButtonImages();

            searchPlayersButton.setIcon(getButtonImage(IMG_NAME_PROFILE_SEARCH, searchPlayersButton.getWidth(), searchPlayersButton.getHeight()));

        } catch (Exception e) {
            System.out.println(e);
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
        displayChallengeEntries();
    }

    /**
     * Adds specific listeners to certain swing items
     */
    @Override
    protected void setupListeners() {
        super.setupListeners();
        setupMenuButtonListeners();

        libraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerSearchWindow != null) {
                    playerSearchWindow.dispose();
                }
            }
        });

        homepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerSearchWindow != null) {
                    playerSearchWindow.dispose();
                }
            }
        });

        socialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerSearchWindow != null) {
                    playerSearchWindow.dispose();
                }
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerSearchWindow != null) {
                    playerSearchWindow.dispose();
                }
            }
        });


        searchPlayersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerSearchWindow != null) {
                    playerSearchWindow.dispose();
                }
                setupPlayerSearchWindow();
                new PlayerSearchScene(playerSearchWindow, app);
            }
        });

        friendSearchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (friendSearchField.getText().equals("search")) {
                    friendSearchField.setText("");
                    friendSearchField.setForeground(CustomColor.GREEN_2);
                }
            }
        });

        friendSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Check if the pressed key is the Enter key
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    findFriendSearchResults();
                }
            }
        });

    }

    /**
     * find and display friends according to a players search results.
     */
    private void findFriendSearchResults() {
        String enteredText = friendSearchField.getText();

        friendsEntriesPanel.removeAll();
        friendsEntriesPanel.revalidate();
        friendsEntriesPanel.repaint();

        ArrayList<String> visiblePlayers = new ArrayList<>();

        for (String player : friends) { // TODO replace with friends from
            if (player.toLowerCase().contains(enteredText.toLowerCase().replace(" ", ""))) {
                visiblePlayers.add(player);
            }
        }

        //createFriendInfoPanels(visiblePlayers); // TODO update search results
    }

    /**
     * Create a new window where a player can search for a player to add as a friend.
     */
    private void setupPlayerSearchWindow() {
        playerSearchWindow = new JFrame();
        playerSearchWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        playerSearchWindow.setBounds((int) (screenSize.getWidth() * 0.25), (int) (screenSize.getHeight() * 0.25), (int) (screenSize.getWidth() * 0.50), (int) (screenSize.getHeight() * 0.50));
        playerSearchWindow.setTitle("Player Search");
        playerSearchWindow.setResizable(false);
        playerSearchWindow.setVisible(true);
        playerSearchWindow.requestFocusInWindow();
    }
}
