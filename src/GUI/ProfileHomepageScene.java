package GUI;

import leaderboard.GameStatistics;
import leaderboard.Leaderboard;
import match_making.Game;
import networking.Client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Displays UI components for the players homepage.
 */

public class ProfileHomepageScene extends Scene {
    public static final String AVATARS_PATH = "avatars/";
    public static final String AVATAR_EXTENSION = ".png";

    private final JPanel statsPanel = new JPanel();
    private final JTextField statsField = new JTextField();
    private final JPanel statsEntriesPanel = new JPanel();
    private JScrollPane statsScrollPane = new JScrollPane();

    private final JPanel personalizedPanel = new JPanel();

    private final JPanel titlePanel = new JPanel();
    private JButton avatarButton = new JButton();
    private final JTextField usernameField = new JTextField();

    private final JPanel profileBioPanel = new JPanel();
    private final JTextField bioTextField = new JTextField();
    private final JTextArea bioTextArea = new JTextArea();

    private JFrame avatarSelectWindow;

    private final ArrayList<String> avatarsNames = new ArrayList<>();

    public ProfileHomepageScene(App app) {
        super(app);
        initAvatars();
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
        homepageButton.setBorder(new LineBorder(CustomColor.DARK_BLUE_2, 3));

        personalizedPanel.setLayout(new BorderLayout());

        titlePanel.setLayout(new FlowLayout());
        titlePanel.setBackground(CustomColor.BLUE_2);

        int buttonSize = (int) (this.getHeight() * 0.17);

        avatarButton = createButton("AvatarSelect", buttonSize, buttonSize);

        usernameField.setText(client.getUsername());
        usernameField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.033)));
        usernameField.setFocusable(false);
        usernameField.setBorder(null);
        usernameField.setForeground(CustomColor.GREEN_1);
        usernameField.setBackground(CustomColor.BLUE_2);

        titlePanel.add(avatarButton);
        titlePanel.add(usernameField);

        bioTextField.setText("Bio");
        bioTextField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.033)));
        bioTextField.setFocusable(false);
        bioTextField.setBorder(null);
        bioTextField.setForeground(CustomColor.GREEN_1);
        bioTextField.setBackground(CustomColor.BLUE_2);

        bioTextArea.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.02)));
        bioTextArea.setText(client.getBio());
        bioTextArea.setForeground(CustomColor.DARK_BLUE_1);
        bioTextArea.setBackground(CustomColor.WHITE_1);

        profileBioPanel.setLayout(new BorderLayout());
        profileBioPanel.add(bioTextArea, BorderLayout.CENTER);
        profileBioPanel.add(bioTextField, BorderLayout.NORTH);

        personalizedPanel.add(titlePanel, BorderLayout.NORTH);
        personalizedPanel.add(profileBioPanel);


        statsPanel.setBackground(CustomColor.BLUE_2);
        statsPanel.setLayout(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createEmptyBorder((int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.02), 0, (int) (this.getWidth() * 0.02)));

        statsField.setText("Stats");
        statsField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.033)));
        statsField.setFocusable(false);
        statsField.setBorder(null);
        statsField.setForeground(CustomColor.GREEN_2);
        statsField.setBackground(CustomColor.BLUE_2);

        statsEntriesPanel.setBackground(CustomColor.BLUE_1);
        statsEntriesPanel.setLayout(new GridBagLayout());
        statsEntriesPanel.setBorder(BorderFactory.createEmptyBorder((int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.01), (int) (this.getHeight() * 0.02), 0));

        statsScrollPane = new JScrollPane(statsEntriesPanel);
        statsScrollPane.setBackground(CustomColor.BLUE_2);
        statsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        statsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        statsScrollPane.setBorder(new BevelBorder(1, CustomColor.DARK_BLUE_2, CustomColor.DARK_BLUE_1));
        editUIScrollPane(statsScrollPane);

        statsPanel.add(statsField, BorderLayout.NORTH);
        statsPanel.add(statsScrollPane, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.NORTH);
        add(personalizedPanel, BorderLayout.WEST);
        add(statsPanel, BorderLayout.CENTER);

        addStatsEntries();
    }

    /**
     * Creates panels for each game.
     */
    private void addStatsEntries() {
        statsEntriesPanel.removeAll();
        statsEntriesPanel.revalidate();
        statsEntriesPanel.repaint();

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;

        Leaderboard leaderboard;

        for (int i = 0; i < Game.values().length; i++) {
            Game game = Game.values()[i];
            leaderboard = client.requestLeaderboard(game);
            gbc.gridy = i;
            statsEntriesPanel.add(getDisplayInfo(leaderboard.getData(), game.toString() + ":"), gbc);
        }
    }

    /**
     * Creates the inner panel for a game.
     *
     * @param stats array of stats
     * @param gameName name of the game
     * @return the panel to be added to stats
     */
    private JPanel getDisplayInfo(ArrayList<GameStatistics> stats, String gameName) {
        JPanel statsPanelIndividual = new JPanel();
        statsPanelIndividual.setLayout(new GridBagLayout());
        statsPanelIndividual.setBackground(CustomColor.BLUE_1);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        int wins = 0;
        int gamesPlayed = 0;
        int timePlayed = 0;
        int elo = 0;
        float winRate = 0;

        for (var stat : stats) {
            if (stat.getUsername().equals(client.getUsername())) {
                wins = stat.getWins();
                gamesPlayed = stat.getGamesPlayed();
                timePlayed = stat.getTimePlayed();
                elo = stat.getELO();
                winRate = stat.getWinRate();
            }
        }

        JTextField gameTitle = new JTextField(gameName);
        gameTitle.setBackground(CustomColor.GREEN_2);
        gameTitle.setForeground(CustomColor.WHITE_1);
        gameTitle.setFocusable(false);

        JTextField winsField = new JTextField("Wins: " + wins);
        winsField.setBackground(CustomColor.WHITE_1);
        winsField.setForeground(CustomColor.GREEN_1);
        winsField.setFocusable(false);

        JTextField gamesPlayedField = new JTextField("Games Played: " + gamesPlayed);
        gamesPlayedField.setBackground(CustomColor.WHITE_1);
        gamesPlayedField.setForeground(CustomColor.GREEN_1);
        gamesPlayedField.setFocusable(false);

        JTextField timePlayedField = new JTextField("Time Played: " + timePlayed);
        timePlayedField.setBackground(CustomColor.WHITE_1);
        timePlayedField.setForeground(CustomColor.GREEN_1);
        timePlayedField.setFocusable(false);

        JTextField eloField = new JTextField("Elo: " + elo);
        eloField.setBackground(CustomColor.WHITE_1);
        eloField.setForeground(CustomColor.GREEN_1);
        eloField.setFocusable(false);

        JTextField winRateField = new JTextField("Win Rate: " + winRate);
        winRateField.setBackground(CustomColor.WHITE_1);
        winRateField.setForeground(CustomColor.GREEN_1);
        winRateField.setFocusable(false);

        gbc.gridy = 0;
        statsPanelIndividual.add(gameTitle, gbc);
        gbc.gridy = 1;
        statsPanelIndividual.add(winsField, gbc);
        gbc.gridy = 2;
        statsPanelIndividual.add(gamesPlayedField, gbc);
        gbc.gridy = 3;
        statsPanelIndividual.add(timePlayedField, gbc);
        gbc.gridy = 4;
        statsPanelIndividual.add(eloField, gbc);
        gbc.gridy = 5;
        statsPanelIndividual.add(winRateField, gbc);

        return statsPanelIndividual;
    }


    /**
     * Resize components not already resized by a layout.
     */
    @Override
    protected void resizeComponents() {

    }

    @Override
    protected void updateScene() {
        addStatsEntries();
    }

    private void initAvatars() {
        avatarsNames.add("GreenSnorkelAvatar");
        avatarsNames.add("BlueSnorkelAvatar");
        avatarsNames.add("FishAvatar");
        avatarsNames.add("PenguinAvatar");
        avatarsNames.add("SharkAvatar");
        avatarsNames.add("TurtleAvatar");
        avatarsNames.add("StarfishAvatar");
        avatarsNames.add("WeirdFishAvatar");
        avatarsNames.add("CatFish");
    }

    /**
     * Assigns images to components.
     */
    @Override
    public void assignImages() {
        try {
            addMenuButtonImages();
            avatarButton.setIcon(getButtonImage(AVATARS_PATH + avatarsNames.get(client.getAvatar()) + AVATAR_EXTENSION, avatarButton.getWidth(), avatarButton.getHeight()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    /**
     * Adds specific listeners to certain swing items
     */
    @Override
    protected void setupListeners() {
        super.setupListeners();
        setupMenuButtonListeners();

        bioTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && bioTextArea.isFocusOwner()) {
                    updateBioLocal();
                }

                updateScene();
            }
        });

        libraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (avatarSelectWindow != null) {
                    avatarSelectWindow.dispose();
                }

                updateScene();
            }
        });

        homepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (avatarSelectWindow != null) {
                    avatarSelectWindow.dispose();
                }

                updateScene();
            }
        });

        socialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (avatarSelectWindow != null) {
                    avatarSelectWindow.dispose();
                }

                updateScene();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (avatarSelectWindow != null) {
                    avatarSelectWindow.dispose();
                }

                updateScene();
            }
        });

        avatarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (avatarSelectWindow != null) {
                    avatarSelectWindow.dispose();
                }
                setupAvatarSelectWindow();
                new AvatarSelectScene(avatarSelectWindow, app, avatarsNames, avatarButton);
            }
        });
    }

    /**
     * Creates a new window for selecting an avatar.
     */
    private void setupAvatarSelectWindow() {
        avatarSelectWindow = new JFrame();
        avatarSelectWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        avatarSelectWindow.setBounds((int) (screenSize.getWidth() * 0.25), (int) (screenSize.getHeight() * 0.25), (int) (screenSize.getWidth() * 0.50), (int) (screenSize.getHeight() * 0.50));
        avatarSelectWindow.setTitle("Avatar Select");
        avatarSelectWindow.setResizable(false);
        avatarSelectWindow.setVisible(true);
        avatarSelectWindow.requestFocusInWindow();
    }

    /**
     * Calls client method to update bio.
     */
    private void updateBioLocal() {
        client.updateBio(bioTextArea.getText());
    }
}
