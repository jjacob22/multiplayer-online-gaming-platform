package GUI;

import networking.Client;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Displays UI components for editing a players profile.
 */
public class ProfileSettingsScene extends Scene {

    private final JPanel changePanels = new JPanel();

    private final JPanel usernamePanel = new JPanel();
    private final JPanel emailPanel = new JPanel();
    private final JPanel passwordPanel = new JPanel();

    private final JTextField usernameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField passwordField = new JTextField();

    private final JTextField changeUsernameField = new JTextField();
    private final JTextField changeEmailField = new JTextField();
    private final JTextField changePasswordField = new JTextField();

    private final JPanel extraComponentsPanel = new JPanel();
    private final JButton logoutAndExitButton = new JButton();

    public ProfileSettingsScene(App app) {
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

        usernameField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.04)));
        usernameField.setText("New Username");
        usernameField.setBackground(CustomColor.BLUE_2);
        usernameField.setForeground(CustomColor.GREEN_2);
        usernameField.setBorder(null);
        usernameField.setFocusable(false);

        emailField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.04)));
        emailField.setText("New Email");
        emailField.setBackground(CustomColor.BLUE_2);
        emailField.setForeground(CustomColor.GREEN_2);
        emailField.setBorder(null);
        emailField.setFocusable(false);

        passwordField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.04)));
        passwordField.setText("New Password");
        passwordField.setBackground(CustomColor.BLUE_2);
        passwordField.setForeground(CustomColor.GREEN_2);
        passwordField.setBorder(null);
        passwordField.setFocusable(false);

        changeUsernameField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.04)));
        changeUsernameField.setText("New Username");
        changeUsernameField.setBackground(CustomColor.WHITE_1);
        changeUsernameField.setForeground(CustomColor.GRAY);

        changeEmailField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.04)));
        changeEmailField.setText("New Email");
        changeEmailField.setBackground(CustomColor.WHITE_1);
        changeEmailField.setForeground(CustomColor.GRAY);

        changePasswordField.setFont(new Font("", Font.PLAIN, (int) (this.getHeight() * 0.04)));
        changePasswordField.setText("New Password");
        changePasswordField.setBackground(CustomColor.WHITE_1);
        changePasswordField.setForeground(CustomColor.GRAY);

        changePanels.setLayout(new GridLayout(5, 1));
        changePanels.setBackground(CustomColor.BLUE_2);

        usernamePanel.setLayout(new GridBagLayout());
        usernamePanel.setBackground(CustomColor.BLUE_2);

        emailPanel.setLayout(new GridBagLayout());
        emailPanel.setBackground(CustomColor.BLUE_2);

        passwordPanel.setLayout(new GridBagLayout());
        passwordPanel.setBackground(CustomColor.BLUE_2);

        int textFieldWidth = (int) (this.getWidth() * 0.10);
        int textFieldHeight = (int) (this.getHeight() * 0.04);

        usernameField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));
        emailField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));
        passwordField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));

        textFieldWidth = (int) (this.getWidth() * 0.90);
        textFieldHeight = (int) (this.getHeight() * 0.04);

        changeUsernameField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));
        changeEmailField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));
        changePasswordField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;

        usernamePanel.add(usernameField, gbc);
        emailPanel.add(emailField, gbc);
        passwordPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.65;
        usernamePanel.add(changeUsernameField, gbc);
        emailPanel.add(changeEmailField, gbc);
        passwordPanel.add(changePasswordField, gbc);

        changePanels.add(usernamePanel);
        changePanels.add(emailPanel);
        changePanels.add(passwordPanel);

        initializeMenuButtons();
        settingsButton.setBorder(new LineBorder(CustomColor.DARK_BLUE_2, 3));



        logoutAndExitButton.setFocusable(false);
        logoutAndExitButton.setText("Logout and Exit");
        logoutAndExitButton.setBackground(CustomColor.RED);
        logoutAndExitButton.setForeground(CustomColor.WHITE_1);

        extraComponentsPanel.setBackground(CustomColor.BLUE_2);
        extraComponentsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        extraComponentsPanel.add(logoutAndExitButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(changePanels, BorderLayout.CENTER);
        add(extraComponentsPanel, BorderLayout.SOUTH);

    }

    /**
     * Assigns images to components.
     */
    @Override
    protected void assignImages() {
        try {
            addMenuButtonImages();

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

    }

    /**
     * Adds specific listeners to components.
     */
    @Override
    protected void setupListeners() {
        super.setupListeners();
        setupMenuButtonListeners();

        logoutAndExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.LogOut();
                System.exit(0);
            }
        });

        changeUsernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (changeUsernameField.getText().equals("New Username")) {
                    changeUsernameField.setText("");
                }
            }
        });

        changeEmailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (changeEmailField.getText().equals("New Email")) {
                    changeEmailField.setText("");
                }
            }
        });

        changePasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (changePasswordField.getText().equals("New Password")) {
                    changePasswordField.setText("");
                }
            }
        });

        changeUsernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (changeUsernameField.getText().isEmpty()) {
                    changeUsernameField.setText("New Username");
                }
            }
        });

        changeEmailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (changeEmailField.getText().isEmpty()) {
                    changeEmailField.setText("New Email");
                }
            }
        });

        changePasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (changePasswordField.getText().isEmpty()) {
                    changePasswordField.setText("New Password");
                }
            }
        });

        changeUsernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && changeUsernameField.isFocusOwner()) {
                    String newUsername = changeUsernameField.getText();
                    // TODO Integration set new username here
                    if (!client.updateUsername(client.getUsername(), newUsername)) {
                        // TODO better error message
                        System.out.println("Failed to update username");
                    }
                    changeUsernameField.setText("");
                }
            }
        });

        changeEmailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && changeEmailField.isFocusOwner()) {
                    String newEmail = changeEmailField.getText();
                    // TODO need TFA code sent
                    // client.updateEmail(newEmail, );

                    changeEmailField.setText("");
                }
            }
        });

        changePasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && changePasswordField.isFocusOwner()) {
                    String newPassword = changePasswordField.getText();
                    // TODO Integration set new password here
                    // TODO Need TFA code sent

                    changePasswordField.setText("");
                }
            }
        });
    }
}
