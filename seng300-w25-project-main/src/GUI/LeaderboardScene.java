package GUI;

import leaderboard.GameStatistics;
import leaderboard.Leaderboard;
import match_making.Game;
import networking.Client;
import networking.server_events.MatchFoundEvent;
import networking.server_events.ServerEventListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

public class LeaderboardScene extends Scene {

    private JPanel leaderboardPanel;
    private JButton startGameButton;
    private JButton backButton;
    private JScrollPane usersArea;
    private JLabel gameNameLabel;
    private JPanel userListPanel;

    // Using the pixel art theme color palette from the image

    // Inside your class:
    private final Color BACKGROUND_COLOR = CustomColor.DARK_BLUE_1;        // (15, 40, 55)
    private final Color PANEL_COLOR = CustomColor.BLUE_2;                  // (30, 75, 93)
    private final Color ACCENT_COLOR = CustomColor.GREEN_1;                // (130, 180, 165)
    private final Color SECONDARY_ACCENT = CustomColor.WHITE_2;            // (180, 210, 220)
    private final Color TEXT_COLOR = CustomColor.WHITE_1;                  // (210, 225, 230)
    private final Color SECONDARY_TEXT_COLOR = CustomColor.GRAY;           // (150, 170, 180)
    private final Color HIGHLIGHT_COLOR = CustomColor.BLUE_1;              // (70, 130, 150)
    private final Color BUTTON_COLOR = CustomColor.GREEN_2;                // (100, 160, 145)
    private final Color POSITIVE_BUTTON_COLOR = CustomColor.GREEN;         // (120, 189, 130)

    // Rank colors
    private final Color GOLD_COLOR = new Color(217, 201, 127);         // Gold for 1st place
    private final Color SILVER_COLOR = new Color(186, 200, 183);       // Silver for 2nd place
    private final Color BRONZE_COLOR = new Color(169, 151, 95);        // Bronze for 3rd place

    // Font
    private final Font FONT = new Font("Monospaced", Font.PLAIN, 16);

    private Timer timer;
    private boolean isQueued = false;
    private Leaderboard leaderboard;

    private String selectedGame;

    private boolean sortedGreatestToLeast = false;
    private boolean isFriendFiltered = false;


    public LeaderboardScene(App app, String selectedGame) {
        super(app);
        this.selectedGame = selectedGame;
        this.leaderboard = client.requestLeaderboard(Game.valueOf(selectedGame));
        initializeComponents();
        setupListeners();
        displayUsers(leaderboard.getSortedBy(true, GameStatistics::getELO).getData());
    }

    @Override
    protected void initializeComponents() {
        setupMainPanel();
        JPanel headerPanel = createHeaderPanel();
        JPanel containerPanel = createContentContainerPanel();
        JPanel footerPanel = createFooterPanel();

        // Add all sections to main panel
        leaderboardPanel.add(headerPanel, BorderLayout.NORTH);
        leaderboardPanel.add(containerPanel, BorderLayout.CENTER);
        leaderboardPanel.add(footerPanel, BorderLayout.SOUTH);

        this.add(leaderboardPanel);
        this.setPreferredSize(new Dimension(800, 600));
    }


    private void setupMainPanel() {
        // Initialize main panel with border layout
        leaderboardPanel = new JPanel(new BorderLayout(20, 20));
        leaderboardPanel.setBackground(BACKGROUND_COLOR);
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
    }

    private void startTimer() {
        timer = new Timer();
        TimerTask task = new MatchQueryTask(client, Game.valueOf(selectedGame));
        timer.schedule(task, 0, 2000);
    }

    private void cancelTimer() {
        if (timer == null) return;
        timer.cancel();
        timer.purge();
    }

    private JPanel createHeaderPanel() {
        // Create header panel for game title and back button
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setOpaque(false);

        // Set up game name label with pixel art styling
        JLabel gameNameLabel = new JLabel(selectedGame + " Leaderboard");
        gameNameLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        gameNameLabel.setForeground(SECONDARY_ACCENT);

        // Create pixel art styled back button
        JButton backButton = createPixelStyledButton("← Back", BUTTON_COLOR);
        backButton.addActionListener(e -> {
            cancelTimer();
            showScene(App.GAME_LIBRARY);
        });

        // Add game name and back button to header
        headerPanel.add(gameNameLabel, BorderLayout.CENTER);
        headerPanel.add(backButton, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createContentContainerPanel() {
        // Create a container panel with pixel art styling
        JPanel containerPanel = new JPanel(new BorderLayout(0, 15));
        containerPanel.setBackground(PANEL_COLOR);
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new CompoundBorder(
                        new LineBorder(ACCENT_COLOR, 2, false),
                        new LineBorder(PANEL_COLOR, 3, false)
                )
        ));

        // Create leaderboard content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setOpaque(false);

        // Create user list panel
        userListPanel = createUserListPanel();

        // Create styled scroll pane for user list
        JScrollPane usersArea = createScrollPane(userListPanel);

        // Create attractive header for the leaderboard table
        JPanel tableHeader = createTableHeader();

        // Add table header and user list to content panel
        contentPanel.add(tableHeader, BorderLayout.NORTH);
        contentPanel.add(usersArea, BorderLayout.CENTER);

        // Add content panel to container panel
        containerPanel.add(contentPanel, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createUserListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL_COLOR);
        return panel;
    }

    private JScrollPane createScrollPane(JPanel contentPanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Apply custom scrollbar styling
        customizeScrollBar(scrollPane);

        return scrollPane;
    }

    private void customizeScrollBar(JScrollPane scrollPane) {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = SECONDARY_ACCENT;
                this.trackColor = PANEL_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();

                // Draw pixelated-looking thumb
                g2.setColor(thumbColor);
                g2.fillRect(thumbBounds.x + 2, thumbBounds.y + 2,
                        thumbBounds.width - 4, thumbBounds.height - 4);

                // Draw pixel-like border
                g2.setColor(ACCENT_COLOR);
                g2.drawRect(thumbBounds.x + 1, thumbBounds.y + 1,
                        thumbBounds.width - 3, thumbBounds.height - 3);

                g2.dispose();
            }
        });
    }

    private JPanel createFooterPanel() {
        // space the footer from left to right
        JPanel footerPanel = new JPanel(new GridLayout(1, 2));
        footerPanel.setBackground(PANEL_COLOR);

        // sort if friend
        JButton sortButton = createPixelStyledButton("Sort by Friends", SECONDARY_ACCENT);
        sortButton.setFont(FONT);
        sortButton.setHorizontalAlignment(SwingConstants.LEFT);
        sortButton.addActionListener(e -> {
            ArrayList<GameStatistics> stats;
            if (!isFriendFiltered) {
                stats = leaderboard.getFilteredByFriends().getData();
            }
            else{
                stats = leaderboard.getData();
            }
            isFriendFiltered = !isFriendFiltered;
            displayUsers(stats);
        });

        footerPanel.add(sortButton);

        JButton joinQueueButton = createPixelStyledButton("Join Queue", SECONDARY_ACCENT);
        joinQueueButton.setFont(FONT);
        joinQueueButton.addActionListener(e -> {
            if (isQueued) return;
            isQueued = true;
            startTimer();
        });

        footerPanel.add(joinQueueButton);

        JButton leaveQueueButton = createPixelStyledButton("Leave Queue", SECONDARY_ACCENT);
        leaveQueueButton.setFont(FONT);
        leaveQueueButton.addActionListener(e -> {
            if (!isQueued) return;
            isQueued = false;
            cancelTimer();
            client.leaveQueue(Game.valueOf(selectedGame));
        });

        footerPanel.add(leaveQueueButton);

        return footerPanel;
    }

    private JPanel createTableHeader() {
        JPanel headerPanel = new JPanel(new GridLayout(1, 3));
        headerPanel.setBackground(HIGHLIGHT_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 40));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ACCENT_COLOR, 1, false),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        JButton rankLabel = createSimpleButton("Rank");
        rankLabel.setFont(FONT);
        rankLabel.setForeground(TEXT_COLOR);
        rankLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton userLabel = createSimpleButton("Username");
        userLabel.setFont(FONT);
        userLabel.setForeground(TEXT_COLOR);
        userLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton eloLabel = createSimpleButton("ELO");
        eloLabel.setFont(FONT);
        eloLabel.setForeground(TEXT_COLOR);
        eloLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton winRateLabel = createSimpleButton("Win Rate");
        winRateLabel.setFont(FONT);
        winRateLabel.setForeground(TEXT_COLOR);
        winRateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        rankLabel.addActionListener(e -> {
            displayUsers(leaderboard.getSortedBy(sortedGreatestToLeast, GameStatistics::getRank).getData());
            sortedGreatestToLeast = !sortedGreatestToLeast;
        });

        // Add action listeners for sorting buttons
        winRateLabel.addActionListener(e -> {
            // Sort users by wins
            displayUsers(leaderboard.getSortedBy(sortedGreatestToLeast, GameStatistics::getWins).getData());
            sortedGreatestToLeast = !sortedGreatestToLeast;
        });

        eloLabel.addActionListener(e -> {
            // Sort users by ELO
            displayUsers(leaderboard.getSortedBy(sortedGreatestToLeast, GameStatistics::getELO).getData());
            sortedGreatestToLeast = !sortedGreatestToLeast;
        });

        userLabel.addActionListener(e -> {
            // Sort users by name
            var sorted = leaderboard.getData();
            if (sortedGreatestToLeast) {
                sorted.sort(Comparator.comparing(GameStatistics::getUsername).reversed());
            }
            else {
                sorted.sort(Comparator.comparing(GameStatistics::getUsername));
            }
            displayUsers(sorted);
            sortedGreatestToLeast = !sortedGreatestToLeast;
        });


        headerPanel.add(rankLabel);
        headerPanel.add(userLabel);
        headerPanel.add(eloLabel);
        headerPanel.add(winRateLabel);

        return headerPanel;
    }

    private static JButton createSimpleButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createPixelStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(FONT);
        button.setBackground(bgColor);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker(), 2, false),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add pixel art hover effect
        addButtonHoverEffect(button, bgColor);

        return button;
    }

    private void addButtonHoverEffect(JButton button, Color baseColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(SECONDARY_ACCENT, 2, false),
                        BorderFactory.createEmptyBorder(8, 18, 8, 18)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(baseColor.darker(), 2, false),
                        BorderFactory.createEmptyBorder(8, 18, 8, 18)
                ));
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.darker());
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }

    @Override
    protected void setupListeners() {
        client.addServerEventListener(new ServerEventListener() {
            @Override
            public void matchFound(MatchFoundEvent e) {
                cancelTimer();
                var scene = ListOfGames.getGame(getApp(), selectedGame);
                scene.updateBoard(e.getGameState().board());
                showScene(selectedGame);
            }
        });
    }

//    private ArrayList<User> getUsers(ArrayList<GameStatistics> stats) {
//        users.clear();
//        for (GameStatistics s : stats) {
//            users.add(new User(s.getUsername(), s.getELO(), s.getWinRate()));
//        }
//
//        // Sort users by score (highest first)
//        users.sort((u1, u2) -> Integer.compare(u2.elo, u1.elo));
//        return users;
//    }

    private void displayUsers(ArrayList<GameStatistics> stats) {
        userListPanel.removeAll(); // Clear old data

        for (var stat : stats) {
            JPanel userPanel = createUserRow(stat);
            userListPanel.add(userPanel);
        }

        userListPanel.revalidate();
        userListPanel.repaint();
    }


    private JPanel createUserRow(GameStatistics user) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 3));

        int rank = user.getRank();

        // Apply styling based on rank
        styleUserRow(rowPanel, rank);

        // Rank label with pixel art styled badge for top 3
        JLabel rankLabel = createRankLabel(rank);

        // Username label with pixel art styling
        JLabel nameLabel = new JLabel(user.getUsername());
        nameLabel.setFont(FONT);
        nameLabel.setForeground(rank <= 3 ? SECONDARY_ACCENT : TEXT_COLOR);

        // Score label
        JLabel eloLabel = new JLabel(String.valueOf(user.getELO()));
        eloLabel.setFont(FONT);
        eloLabel.setForeground(rank <= 3 ? SECONDARY_ACCENT : TEXT_COLOR);
        eloLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // win rate
        JLabel winRateLabel = new JLabel(String.format("%.2f%%", user.getWinRate() * 100));
        winRateLabel.setFont(FONT);
        winRateLabel.setForeground(rank <= 3 ? SECONDARY_ACCENT : TEXT_COLOR);
        winRateLabel.setHorizontalAlignment(SwingConstants.RIGHT);


        rowPanel.add(rankLabel);
        rowPanel.add(nameLabel);
        rowPanel.add(eloLabel);
        rowPanel.add(winRateLabel);

        return rowPanel;
    }

    private void styleUserRow(JPanel rowPanel, int rank) {
        // Create pixel art style rows
        Color rowColor = rank % 2 == 0 ? PANEL_COLOR : HIGHLIGHT_COLOR;
        rowPanel.setBackground(rowColor);

        // Highlight top 3 rankings with a pixel art styled border
        if (rank <= 3) {
            Border outerBorder = BorderFactory.createMatteBorder(0, 4, 0, 0, getTopRankColor(rank));
            Border innerBorder = BorderFactory.createEmptyBorder(10, 7, 10, 10);
            rowPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        } else {
            rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 11, 10, 10));
        }

        // Add hover effect for rows
        rowPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                rowPanel.setBackground(rowColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rowPanel.setBackground(rowColor);
            }
        });

        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    }

    private JLabel createRankLabel(int rank) {
        // Create pixel art styled rank indicators
        String rankText;
        if (rank == 1) rankText = "♔ " + rank;       // King for 1st place
        else if (rank == 2) rankText = "♕ " + rank;  // Queen for 2nd place
        else if (rank == 3) rankText = "♖ " + rank;  // Rook for 3rd place
        else rankText = String.valueOf(rank);

        JLabel rankLabel = new JLabel(rankText);
        rankLabel.setFont(FONT);
        rankLabel.setForeground(rank <= 3 ? getTopRankColor(rank) : SECONDARY_TEXT_COLOR);

        return rankLabel;
    }

    private Color getTopRankColor(int rank) {
        switch (rank) {
            case 1: return GOLD_COLOR;
            case 2: return SILVER_COLOR;
            case 3: return BRONZE_COLOR;
            default: return SECONDARY_TEXT_COLOR;
        }
    }

    @Override
    protected void assignImages() {
        // Implementation handled in loadGameIcon method
    }

    @Override
    protected void resizeComponents() {
        // Window resize handling if needed
    }

    @Override
    protected void updateScene() {

    }
}