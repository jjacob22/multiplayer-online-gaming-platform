package GUI;

import networking.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountScene extends Scene {
    public static final String BACKGROUND_IMG = "resources/images/backgrounds/LoginBG.png";
    private static final Color DARK_BLUE = new Color(0, 51, 102);
    private static final Font MONOSPACE_FONT = new Font("Monospaced", Font.PLAIN, 14);
    private static final Font MONOSPACE_BOLD = new Font("Monospaced", Font.BOLD, 36);
    private static final Font MONOSPACE_TITLE = new Font("Monospaced", Font.BOLD, 36);

    private final JPanel createAccountPanel;
    private final JPanel divCreateAccountPanel;
    private final JLabel titleLabel;
    private final JTextField usernameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JButton createAccountButton;
    private final JLabel loginLabel;
    private final ImageIcon backgroundImage;

    public CreateAccountScene(App app) {
        super(app);
        backgroundImage = new ImageIcon(BACKGROUND_IMG);

        // Main panel with background image
        createAccountPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        createAccountPanel.setBackground(new Color(0, 0, 0, 0));

        // Rounded translucent panel
        divCreateAccountPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(DARK_BLUE);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                g2.dispose();
            }
        };
        divCreateAccountPanel.setOpaque(false);
        divCreateAccountPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        titleLabel = new JLabel("CREATE ACCOUNT");
        titleLabel.setFont(MONOSPACE_TITLE);
        titleLabel.setForeground(DARK_BLUE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form fields
        usernameField = createRetroTextField("Username");
        emailField = createRetroTextField("Email");
        passwordField = createRetroPasswordField("Password");
        confirmPasswordField = createRetroPasswordField("Confirm Password");

        // Create Account Button
        createAccountButton = new JButton("Create Account") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DARK_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 40);
            }
        };
        createAccountButton.setContentAreaFilled(false);
        createAccountButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        createAccountButton.setFocusPainted(false);
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login link
        loginLabel = new JLabel("Already have an account? Log in");
        loginLabel.setFont(MONOSPACE_FONT);
        loginLabel.setForeground(DARK_BLUE);
        Font font = loginLabel.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        loginLabel.setFont(font.deriveFont(attributes));
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create centered form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(createAccountButton);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(loginLabel);

        // Center the form panel in the rounded panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        divCreateAccountPanel.add(formPanel, gbc);

        // Center the rounded panel in the main panel
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weighty = 1;
        mainGbc.insets = new Insets(200, 0, 0, 0); // 200px top margin
        mainGbc.anchor = GridBagConstraints.CENTER;
        createAccountPanel.add(divCreateAccountPanel, mainGbc);

        this.add(createAccountPanel);
        setupListeners();
    }

    private JTextField createRetroTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!getText().isEmpty() && !getText().equals(placeholder)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.setColor(Color.BLACK);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    Insets insets = getInsets();
                    int x = insets.left;
                    int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(getText(), x, y);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(300, 40));
        field.setOpaque(false);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_BLUE, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setForeground(Color.BLACK);
        field.setFont(MONOSPACE_FONT);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupFieldPlaceholder(field, placeholder);
        return field;
    }

    private JPasswordField createRetroPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getPassword().length > 0 && !String.valueOf(getPassword()).equals(placeholder)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.setColor(Color.BLACK);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    Insets insets = getInsets();
                    int x = insets.left;
                    int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                    if (getEchoChar() == 0) {
                        g2.drawString(String.valueOf(getPassword()), x, y);
                    } else {
                        g2.drawString(new String(new char[getPassword().length]).replace("\0", "•"), x, y);
                    }
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(300, 40));
        field.setOpaque(false);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_BLUE, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setForeground(Color.BLACK);
        field.setFont(MONOSPACE_FONT);
        field.setEchoChar((char)0);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupPasswordFieldPlaceholder(field, placeholder);
        return field;
    }

    private void setupFieldPlaceholder(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void setupPasswordFieldPlaceholder(JPasswordField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                    field.setEchoChar((char)0);
                }
            }
        });
    }

    @Override
    protected void setupListeners() {
        createAccountButton.addActionListener(e -> {
            if (validateFields()) {
                showEmailVerificationDialog();
            }
        });

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showScene(App.LOGIN);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setForeground(new Color(0, 102, 204));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setForeground(DARK_BLUE);
            }
        });
    }

    private boolean validateFields() {
        if (usernameField.getText().isEmpty() || usernameField.getText().equals("Username")) {
            showError("Please enter a username");
            return false;
        }

        if (emailField.getText().isEmpty() || emailField.getText().equals("Email")) {
            showError("Please enter your email");
            return false;
        }

        String password = String.valueOf(passwordField.getPassword());
        if (password.isEmpty() || password.equals("Password")) {
            showError("Please enter a password");
            return false;
        }

        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
        if (confirmPassword.isEmpty() || confirmPassword.equals("Confirm Password")) {
            showError("Please confirm your password");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return false;
        }

        if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
            showError("Please enter a valid email address");
            return false;
        }

        if (!client.userRegister(emailField.getText(), password, usernameField.getText(), confirmPassword)) {
            showError("Registration failed. Please try again.");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(createAccountPanel, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showEmailVerificationDialog() {
        JDialog verificationDialog = new JDialog();
        verificationDialog.setTitle("Email Verification");
        verificationDialog.setSize(400, 200);
        verificationDialog.setLocationRelativeTo(null);
        verificationDialog.setModal(true);
        verificationDialog.setLayout(new BorderLayout());

        JPanel verificationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(DARK_BLUE);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        verificationPanel.setLayout(new BoxLayout(verificationPanel, BoxLayout.Y_AXIS));
        verificationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        verificationPanel.setOpaque(false);

        JLabel infoLabel = new JLabel("Please enter the verification code sent to your email:");
        infoLabel.setForeground(DARK_BLUE);
        infoLabel.setFont(MONOSPACE_FONT);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField codeField = createRetroTextField("Verification Code");
        codeField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton verifyButton = new JButton("Verify") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DARK_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(120, 35);
            }
        };
        verifyButton.setContentAreaFilled(false);
        verifyButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        verifyButton.setFocusPainted(false);
        verifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        verificationPanel.add(infoLabel);
        verificationPanel.add(Box.createVerticalStrut(15));
        verificationPanel.add(codeField);
        verificationPanel.add(Box.createVerticalStrut(15));
        verificationPanel.add(verifyButton);

        verificationDialog.add(verificationPanel, BorderLayout.CENTER);

        verifyButton.addActionListener(e -> {
            if (client.verifySignup(codeField.getText(), emailField.getText(),
                    String.valueOf(passwordField.getPassword()), usernameField.getText(),
                    String.valueOf(confirmPasswordField.getPassword()))) {
                JOptionPane.showMessageDialog(createAccountPanel,
                        "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                showScene(App.LOGIN);
            } else {
                showError("Invalid verification code. Please try again.");
            }
            verificationDialog.dispose();
        });

        verificationDialog.setVisible(true);
    }

    @Override
    protected void initializeComponents() {
        // Already initialized in constructor
    }

    @Override
    protected void assignImages() {
        // Not needed for this implementation
    }

    @Override
    protected void resizeComponents() {
        // Not needed for this implementation
    }

    @Override
    protected void updateScene() {
        // Not needed for this implementation
    }
}