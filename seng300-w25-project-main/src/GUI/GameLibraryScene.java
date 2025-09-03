package GUI;

import networking.Client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Represents a players game library where all games on the system can be found. A player can find a game by searching or
 * scrolling for it. When a game is selected it will take the player to the leaderboard scene where they can start the game.
 * The player can also go to their profile page from here.
 */
public class GameLibraryScene extends Scene {
    public static final String GAME_ICONS_PATH = "game_icons/";
    public static final String GAME_ICON_EXTENSION = ".png";

    private final JPanel searchPanel = new JPanel();
    private final JTextField gameSearchArea = new JTextField();
    private final JPanel gamesPanel = new JPanel();
    private JScrollPane gameScrollPane = new JScrollPane();
    /**
     * This array list seems to be used to help with filtering games by name.
     */
    private final ArrayList<JButton> originalGameButtons = new ArrayList<>();
    /**
     * Base constructor
     */
    public GameLibraryScene(App app) {
        super(app);
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
        setBorder(BorderFactory.createEmptyBorder((int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.02), (int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.02)));

        initializeMenuButtons();
        libraryButton.setBorder(new LineBorder(CustomColor.DARK_BLUE_2, 3));

        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder((int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.02), (int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.02)));
        searchPanel.setBackground(CustomColor.BLUE_2);

        gameSearchArea.setFont(new Font("", Font.PLAIN, 30));
        gameSearchArea.setText("search");
        gameSearchArea.setBackground(CustomColor.WHITE_1);
        gameSearchArea.setForeground(CustomColor.GRAY);

        gamesPanel.setBackground(CustomColor.BLUE_1);
        gamesPanel.setLayout(new GridBagLayout());
        gamesPanel.setBorder(BorderFactory.createEmptyBorder((int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.01), (int) (this.getHeight() * 0.30), 0));



        initGameButtons();

        gameScrollPane = new JScrollPane(gamesPanel);
        gameScrollPane.setBackground(CustomColor.BLUE_2);
        gameScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gameScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gameScrollPane.setBorder(new BevelBorder(1, CustomColor.DARK_BLUE_2, CustomColor.DARK_BLUE_1));
        editUIScrollPane(gameScrollPane);

        searchPanel.add(gameSearchArea, BorderLayout.NORTH);
        searchPanel.add(gameScrollPane, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
    }


    protected void initGameButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        int i = 0;

        for (String gameName : ListOfGames.getGameNames()) {


            JButton gameButton = createButton(gameName);

            if (originalGameButtons.size() != 3) {
                originalGameButtons.add(gameButton); // Used in search
            }

            gbc.gridx = i % 3;
            if (i % 3 == 0) {
                gbc.gridy++;
            }

            gamesPanel.add(gameButton, gbc);

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

            for (Component component : gamesPanel.getComponents()) {
                JButton button = (JButton) component;
                button.setIcon(getButtonImage(GAME_ICONS_PATH + button.getName() + GAME_ICON_EXTENSION, button.getPreferredSize().width, button.getPreferredSize().height));
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Resize components not already resized by a layout.
     */
    @Override
    protected void resizeComponents() {
        int numCols = 3;
        int buttonSize = (int) ((double) gameScrollPane.getWidth() / numCols - gameScrollPane.getVerticalScrollBar().getWidth() - this.getWidth() * 0.02);
        for (Component button : gamesPanel.getComponents()) {
            if (button instanceof JButton) {
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                button.revalidate();
            }
        }

        gamesPanel.setBorder(BorderFactory.createEmptyBorder((int) (this.getHeight() * 0.02), (int) (this.getWidth() * 0.01), (int) (this.getHeight() * 0.30), 0));
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
        setupMenuButtonListeners();

        gameSearchArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (gameSearchArea.getText().equals("search")) {
                    gameSearchArea.setText("");
                    gameSearchArea.setForeground(CustomColor.GREEN_2);
                }
            }
        });

        gameSearchArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Check if the pressed key is the Enter key
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    findGameSearchResults();
                }
            }
        });

        for (JButton button : originalGameButtons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    launchLeaderboard(button.getName());
                }
            });
        }
    }

    /**
     * determine which buttons are valid to display
     */
    private void findGameSearchResults() {
        String enteredText = gameSearchArea.getText();

        gamesPanel.removeAll();
        gamesPanel.revalidate();
        gamesPanel.repaint();

        ArrayList<JButton> displayButtons = new ArrayList<>();

        for (Component gameButton : originalGameButtons) {
            if (gameButton.getName().toLowerCase().contains(enteredText.toLowerCase().replace(" ", ""))) {
                displayButtons.add((JButton) gameButton);
            }
        }

        updateSearchResults(displayButtons);
        resizeComponents();
        assignImages();


    }

    /**
     * puts valid buttons onto the screen
     * @param displayButtons buttons to display
     */
    private void updateSearchResults(ArrayList<JButton> displayButtons) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        int i = 0;

        for (JButton button : displayButtons) {


            JButton gameButton = createButton(button.getName());

            if (originalGameButtons.size() != 3) {
                originalGameButtons.add(gameButton); // Used in search
            }

            gbc.gridx = i % 3;
            if (i % 3 == 0) {
                gbc.gridy++;
            }

            gamesPanel.add(gameButton, gbc);

            i++;
        }
    }
}
