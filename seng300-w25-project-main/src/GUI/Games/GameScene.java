package GUI.Games;

import GUI.App;
import GUI.CustomColor;
import GUI.Scene;
import game.GameStatus;
import game.Piece;
import networking.server_events.ChatMessageEvent;
import networking.server_events.GameStateEvent;
import networking.server_events.GameEventListener;
import networking.server_events.ServerExceptionEvent;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public abstract class GameScene extends Scene {

    protected JSplitPane splitPane;
    protected JPanel game;
    private JPanel gamePanel;
    private JPanel opponent;
    private JPanel textPane;
    private JTextField messageInput;
    private JTextArea opponentName;
    private JPanel gameArea;
    private JScrollPane chatPane;
    private JPanel topOfGame;
    private JPanel botOfGame;
    private JButton sendMessageButton;
    private JTextPane chatMessages;
    private JPanel communicationPanel;

    private LookAndFeel defaultLAF;
    protected final int SIDE_BORDER = 10;
    private final String FONT = "";
    private int player1;

    GameScene (App app) {
        super(app);
        player1 = -1;
        defaultLAF = UIManager.getLookAndFeel();
        nimbusLookAndFeel();

        initializeComponents();
        this.add(gamePanel);
        gamePanel.setBorder(new MatteBorder(0, SIDE_BORDER, 0, SIDE_BORDER, CustomColor.DARK_BLUE_2));


        // Chat Section background
        Color chatBG = CustomColor.DARK_BLUE_2;
        communicationPanel.setBackground(chatBG);
        communicationPanel.setBorder(BorderFactory.createLineBorder(chatBG, 2));
        opponent.setBackground(chatBG);
        opponentName.setBackground(chatBG);
        textPane.setBackground(chatBG);

        // Text Areas and Send Button
        sendMessageButton.setBackground(CustomColor.WHITE_1);
        chatMessages.setBackground(CustomColor.WHITE_1);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gameArea, communicationPanel);
        splitPane.setDividerLocation(this.getWidth() * 7/10);
        splitPane.setResizeWeight(1);
        splitPane.setDividerSize(SIDE_BORDER);
        splitPane.setBackground(chatBG);
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                BasicSplitPaneDivider divider = super.createDefaultDivider();
                divider.setBackground(chatBG);
                return divider;
            }
        });

        game.setPreferredSize(new Dimension(-1, this.getHeight()));
        game.setLayout(new GridLayout(1, 1));

        // TODO get opponents name!!!!!!
        opponentName.setText("Opponents Name Here");
        opponentName.setForeground(CustomColor.WHITE_1);
        opponentName.setFocusable(false);
        opponentName.setBorder(BorderFactory.createLineBorder(CustomColor.DARK_BLUE_2));
        opponentName.setFont(new Font(FONT, Font.PLAIN, this.getHeight() / 60));

        textPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPane.setBorder(BorderFactory.createLineBorder(textPane.getBackground(), 5));

        topOfGame.setBackground(CustomColor.DARK_BLUE_2);

        botOfGame.setBackground(topOfGame.getBackground());

        messageInput.setBackground(CustomColor.WHITE_1);
        messageInput.setFont(new Font(FONT, Font.PLAIN, this.getHeight() / 60));
        messageInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        gameArea.setBackground(CustomColor.DARK_BLUE_2);

        sendMessageButton.setText("Send");
        sendMessageButton.setFocusable(false);
        sendMessageButton.addActionListener(e -> sendMessage());

        chatMessages.setFocusable(false);

        chatPane = new JScrollPane(chatMessages);
        editUIComponents();

        gamePanel.add(splitPane);
        communicationPanel.add(opponent, BorderLayout.PAGE_START);
        communicationPanel.add(chatPane, BorderLayout.CENTER);
        communicationPanel.add(textPane, BorderLayout.PAGE_END);
        opponent.add(opponentName);

        textPane.add(messageInput, BorderLayout.CENTER);
        textPane.add(sendMessageButton, BorderLayout.LINE_END);
        
        gameArea.add(topOfGame, BorderLayout.PAGE_START);
        gameArea.add(game, BorderLayout.CENTER);
        gameArea.add(botOfGame, BorderLayout.PAGE_END);

        addMessage("<Type !surrender to concede>\n", Color.RED);

        setupListeners();
        defaultLookAndFeel();
    }

    protected void nimbusLookAndFeel() {
        try {
            // Set Nimbus
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            // Create Nimbus-styled components
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void defaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel(defaultLAF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Replaces built in scrollbar with custom one.
     */
    private void editUIComponents() {
        BasicScrollBarUI scrollBarUI = new BasicScrollBarUI() {
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
        };

        chatPane.getVerticalScrollBar().setUnitIncrement(16);
        chatPane.getVerticalScrollBar().setBlockIncrement(32);
        chatPane.getVerticalScrollBar().setUI(scrollBarUI);

        chatPane.getHorizontalScrollBar().setUnitIncrement(16);
        chatPane.getHorizontalScrollBar().setBlockIncrement(32);
        chatPane.getHorizontalScrollBar().setUI(scrollBarUI);
    }

    @Override
    protected void initializeComponents() {
        gamePanel = new JPanel(new GridLayout(1, 1));
        opponent = new JPanel(new GridBagLayout());
        textPane = new JPanel(new BorderLayout(5, 0));
        messageInput = new JTextField();
        opponentName = new JTextArea();
        gameArea = new JPanel(new BorderLayout());
        topOfGame = new JPanel();
        botOfGame = new JPanel();
        game = new JPanel(new BorderLayout());
        sendMessageButton = new JButton();
        chatMessages = new JTextPane();
        chatPane = new JScrollPane(chatMessages);
        communicationPanel = new JPanel(new BorderLayout());
    }

    @Override
    protected void setupListeners() {
        client.addGameEventListener(new GameEventListener() {
            @Override
            public void chatMessageReceived(ChatMessageEvent e) {
                addMessage(e.getMessage(), e.getSender());
            }

            @Override
            public void gameStateChanged(GameStateEvent e) {
                var status = e.getGameState().status();
                updateBoard(e.getGameState().board());
                if (status == GameStatus.WIN) {
                    var winner = e.getGameState().players().current().getPlayerID();
                    if (winner == client.getMyID()) {
                        showScene(App.VICTORY);
                    }
                    else {
                        showScene(App.GAME_OVER);
                    }
                }
                else if (status == GameStatus.DRAW) {
                    showScene(App.GAME_OVER);
                }
            }

            @Override
            public void exceptionOccurred(ServerExceptionEvent e) {
                addMessage("<" + e.getExceptionMessage() + ">\n", Color.RED);
            }
        });
    }

    public abstract void updateBoard(Piece[][] board);
    public void initBoard(Piece[][] board, int player1) {
        this.player1 = player1;
        updateBoard(board);
    }

    protected int getPlayer1() {
        return player1;
    }

    /**
     * Adds a message to the chat section
     * @param message the message the player is sending
     * @param playerName name of the player sending the message
     */
    public void addMessage(String message, String playerName) {
        // TODO have messages start at bottom of chatMessages
        // TODO make opponent appear of different side of player
        try {
            StyledDocument doc = chatMessages.getStyledDocument();
            // Bolds player name
            Style boldStyle = chatMessages.addStyle("Bold", null);
            StyleConstants.setBold(boldStyle, true);
            doc.insertString(doc.getLength(), playerName + ": ", boldStyle);
            doc.insertString(doc.getLength(), message + "\n", null);
        } catch(BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * For system messages
     * @param message the message to send, \n needed at end
     * @param color the color of the message
     */
    public void addMessage(String message, Color color) {
        try {
            StyledDocument doc = chatMessages.getStyledDocument();
            // Bolds and colours message
            Style style = chatMessages.addStyle("Bold", null);
            StyleConstants.setBold(style, true);
            StyleConstants.setForeground(style, color);

            // Centers message
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            SimpleAttributeSet left = new SimpleAttributeSet();
            StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);

            doc.setParagraphAttributes(doc.getLength(), 1, center, false);
            doc.insertString(doc.getLength(), message, style);
            doc.setParagraphAttributes(doc.getLength(), 1, left, false);
        } catch(BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits text from messageInput if the field is not empty
     */
    private void sendMessage() {
        if (!messageInput.getText().isEmpty()) {
            if (messageInput.getText().equals("!surrender")) {
                addMessage("<Match Surrendered>", Color.RED);
                client.forfeitMatch();
            } else {
                client.chatMessage(messageInput.getText());
            }
            messageInput.setText(null);
        }
    }
}
