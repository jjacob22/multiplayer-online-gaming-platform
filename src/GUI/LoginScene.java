package GUI;

import networking.Client;
import profile.UserAuthentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class LoginScene extends Scene {
    public static final String BACKGROUND_IMG = "resources/images/backgrounds/LoginBG.png";
    private static final Color DARK_BLUE = new Color(0, 51, 102);
    private static final Font MONOSPACE_FONT = new Font("Monospaced", Font.PLAIN, 14);
    private static final Font MONOSPACE_BOLD = new Font("Monospaced", Font.BOLD, 36);
    private static final Font MONOSPACE_TITLE = new Font("Monospaced", Font.BOLD, 36);

    private final JPanel loginPanel;
    private final JPanel divLoginPanel;
    private final JLabel loginTitle;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton submitButton;
    private final JLabel createAccountLabel;
    private final JButton createAccountButton;
    private final ImageIcon backgroundImage;
    private final JLabel forgotPassword;
    private JFrame forgotPasswordPopup;


    public LoginScene(App app) {
        super(app);
        backgroundImage = new ImageIcon(BACKGROUND_IMG);

        // Main panel with background image
        loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        loginPanel.setBackground(new Color(0, 0, 0, 0));

        // Rounded translucent panel
        divLoginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 180)); // Less transparent than CreateAccountScene
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(DARK_BLUE);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                g2.dispose();
            }
        };
        divLoginPanel.setLayout(new BoxLayout(divLoginPanel, BoxLayout.PAGE_AXIS));
        divLoginPanel.setOpaque(false);
        divLoginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        loginTitle = new JLabel("LOGIN");
        loginTitle.setFont(MONOSPACE_TITLE);
        loginTitle.setForeground(DARK_BLUE);
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form fields
        usernameField = createRetroTextField("Username");
        passwordField = createRetroPasswordField("Password");

        // Submit Button
        submitButton = new JButton("Login") {
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
        submitButton.setContentAreaFilled(false);
        submitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        forgotPassword = new JLabel("<html><a href=''>Forgot Password?</a></html>");
        forgotPassword.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));

        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (forgotPasswordPopup != null) {
                    forgotPasswordPopup.dispose();
                }

                forgotPasswordPopup = new JFrame();
                forgotPasswordPopup.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                forgotPasswordPopup.setBounds((int) (getWidth() * 0.35), (int) (getHeight() * 0.25), (int) (getWidth() * 0.45), (int) (getHeight() * 0.35));
                forgotPasswordPopup.setTitle("Password Recovery");
                forgotPasswordPopup.setResizable(false);
                forgotPasswordPopup.setVisible(true);

                JPanel forgotPasswordPanel = setForgotPasswordPopup(forgotPasswordPopup);
                forgotPasswordPopup.add(forgotPasswordPanel);
                forgotPasswordPopup.requestFocusInWindow();

            }
        });


        // Create Account link and button
        createAccountLabel = new JLabel("Don't have an account?");
        createAccountLabel.setFont(MONOSPACE_FONT);
        createAccountLabel.setForeground(DARK_BLUE);
        createAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        // Layout components
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createVerticalStrut(20));
        verticalBox.add(loginTitle);
        verticalBox.add(Box.createVerticalStrut(30));
        verticalBox.add(usernameField);
        verticalBox.add(Box.createVerticalStrut(15));
        verticalBox.add(passwordField);
        verticalBox.add(Box.createVerticalStrut(30));
        verticalBox.add(submitButton);
        verticalBox.add(Box.createVerticalStrut(15));
        verticalBox.add(forgotPassword);
        verticalBox.add(Box.createVerticalStrut(15));
        verticalBox.add(createAccountLabel);
        verticalBox.add(Box.createVerticalStrut(5));
        verticalBox.add(createAccountButton);

        divLoginPanel.add(verticalBox);

        // Position the panel below OMG heading (200px from top)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.insets = new Insets(200, 0, 0, 0); // 200px top margin
        loginPanel.add(divLoginPanel, gbc);

        this.add(loginPanel);
        setupListeners();
    }
    /**
     * Function to create how the text field and the text being input would look like when input is being entered
     * @param placeholder
     * @return the text field to be displayed
     */
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

    /**
     * Function to create how the password field and the text being input would look like when input is being entered
     * @param placeholder
     * @return the password field to be displayed
     */
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

    /**
     * Function  to set up the popup screen that appears when forgot password hyperlink is clicked
     * @param forgotPasswordPopup
     * @return the panel to be added to the popup window
     */
    private JPanel setForgotPasswordPopup(JFrame forgotPasswordPopup){
        //Setting up label that contains the text to be displayed at the top of the popup
        JLabel Info = new JLabel("Enter your username to get reset code sent to your associated email");
        Info.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
        Info.setForeground(CustomColor.WHITE_2);
        Info.setBorder(BorderFactory.createEmptyBorder(20,0,10,0));
        Info. setAlignmentX(Component.CENTER_ALIGNMENT);

        //Setting up the text field for the username
        JTextField usernameFieldPopup = createTextField("Username");
        usernameFieldPopup.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Setting up the submit button the user clicks after entering the username
        Box submitButtonBox = Box.createVerticalBox();
        JButton submitButton = new JButton("Submit");
        submitButton.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        submitButton.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
        submitButton.setPreferredSize(new Dimension(100, 30));
        submitButton.setMaximumSize(new Dimension(100, 30));
        submitButton.setBackground(CustomColor.BLUE_1);
        submitButton.setBorder(BorderFactory.createLineBorder(CustomColor.DARK_BLUE_1,3));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButtonBox.add(submitButton);
        submitButtonBox.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        //Setting up the panel that would be added to the popup which would contain all the components
        JPanel forgotPasswordPanel = new JPanel();
        forgotPasswordPanel.setLayout(new BoxLayout(forgotPasswordPanel, BoxLayout.Y_AXIS));
        forgotPasswordPanel.setBackground(CustomColor.BLUE_2);

        forgotPasswordPanel.add(Info);
        forgotPasswordPanel.add(usernameField);
        forgotPasswordPanel.add(submitButtonBox);

        Box codeFieldBox = Box.createVerticalBox();
        JTextField codeField = createTextField("Code");
        codeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        codeFieldBox.add(codeField);
        codeFieldBox.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        Box codeSubmitButtonBox = Box.createVerticalBox();
        JButton codeSubmitButton = new JButton("Submit Code");
        codeSubmitButton.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
        codeSubmitButton.setPreferredSize(new Dimension(100,30));
        codeSubmitButton.setBackground(CustomColor.BLUE_1);
        codeSubmitButton.setBorder(BorderFactory.createLineBorder(CustomColor.DARK_BLUE_1,3));
        codeSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        codeSubmitButtonBox.add(codeSubmitButton);
        codeSubmitButtonBox.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!checkIfTextFieldEmpty(usernameField, "Username", "Please enter your username", forgotPasswordPopup)) return;
                forgotPasswordPanel.add(codeFieldBox);
                forgotPasswordPanel.add(codeSubmitButtonBox);
                String code = codeField.getText();
                int userID = client.getMyID();

                try {
                    UserAuthentication.forgotPassword(userID);
                    forgotPasswordPanel.revalidate();
                    forgotPasswordPanel.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Code unable to send because user does not exist", null, JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        Box newPasswordFieldBox = Box.createVerticalBox();
        JPasswordField newPasswordField = createPasswordField("New Password");
        newPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPasswordFieldBox.add(newPasswordField);
        newPasswordFieldBox.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        Box confirmNewPasswordFieldBox = Box.createVerticalBox();
        JPasswordField confirmNewPasswordField = createPasswordField("Confirm New Password");
        confirmNewPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmNewPasswordFieldBox.add(confirmNewPasswordField);
        confirmNewPasswordFieldBox.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        JButton newPasswordSubmitButton = new JButton("Submit");
        Box newPasswordSubmitBox = Box.createVerticalBox();
        newPasswordSubmitButton.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        newPasswordSubmitButton.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
        newPasswordSubmitButton.setPreferredSize(new Dimension(100, 30));
        newPasswordSubmitButton.setMaximumSize(new Dimension(100, 30));
        newPasswordSubmitButton.setBackground(CustomColor.BLUE_1);
        newPasswordSubmitButton.setBorder(BorderFactory.createLineBorder(CustomColor.DARK_BLUE_1,3));
        newPasswordSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPasswordSubmitBox.add(newPasswordSubmitButton);
        newPasswordSubmitBox.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        codeSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!checkIfTextFieldEmpty(codeField, "Code", "Please enter the code sent to your email", forgotPasswordPopup)) return;
                forgotPasswordPanel.remove(Info);
                forgotPasswordPanel.remove(usernameFieldPopup);
                forgotPasswordPanel.remove(submitButtonBox);
                forgotPasswordPanel.remove(codeFieldBox);
                forgotPasswordPanel.remove(codeSubmitButtonBox);

                forgotPasswordPanel.add(newPasswordFieldBox);
                forgotPasswordPanel.add(confirmNewPasswordFieldBox);
                forgotPasswordPanel.add(newPasswordSubmitBox);
                forgotPasswordPanel.revalidate();
                forgotPasswordPanel.repaint();

            }
        });

        newPasswordSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameFieldPopup.getText();
                char[] newPassword = newPasswordField.getPassword();
                String newPasswordString = String.valueOf(newPassword);
                char[] confirmNewPassword = confirmNewPasswordField.getPassword();
                String confirmNewPassswordSring = String.valueOf(confirmNewPassword);
                if(!checkIfPasswordFieldEmpty(newPasswordField, forgotPasswordPopup)) return;
                if(!checkIfPasswordFieldEmpty(confirmNewPasswordField, forgotPasswordPopup)) return;
                try {
                    UserAuthentication.resetPassword(username, codeField.getText(), newPasswordString,confirmNewPassswordSring);
                    forgotPasswordPopup.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Password reset error. Try again", null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        return forgotPasswordPanel;
    }

    /**
     * function to check and display error message if the text field is empty as this is invalid input
     * @param field
     * @param placeholder
     * @param message
     * @param parent
     * @return
     */
    private boolean checkIfTextFieldEmpty(JTextField field, String placeholder, String message, Component parent){
        String text = field.getText();
        if (text.isEmpty() || text.equals(placeholder)) {
            JOptionPane.showMessageDialog(parent, message, "Missing Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * function to check and display error message if the password field is empty as this is invalid input
     * @param field
     * @param parent
     * @return
     */
    private boolean checkIfPasswordFieldEmpty(JPasswordField field, Component parent){
        char[] password = field.getPassword();
        if(password.length ==0 || String.valueOf(password).equals("Password") || String.valueOf(password).equals("Confirm New Password")) {
            JOptionPane.showMessageDialog(parent, "Please enter your password", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    /**
     * This method creates a text field with Username text displayed inside the field
     * @param placeholder
     * @return
     */
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setForeground(Color.GRAY);
        field.setPreferredSize(new Dimension(300, 30));
        field.setMaximumSize(new Dimension(300, 30));
        field.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CustomColor.DARK_BLUE_1, 3),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

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

        return field;
    }

    /**
     * This method creates a password field with Password text displayed inside the field
     * @param placeholder  text to be displayed in field
     * @return  the password field to be displayed
     */
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setPreferredSize(new Dimension(300,30));
        field.setMaximumSize(new Dimension(300,30));
        field.setFont(MONOSPACE_FONT.deriveFont(Font.BOLD));
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CustomColor.DARK_BLUE_1, 3),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        field.setEchoChar((char)0);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•'); // Show bullets when typing
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

        return field;
    }

    /**
     * function to set up the field with a placeholder stating what should be input to the field
     * @param field
     * @param placeholder
     */
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

    /**
     * function to set up the field with a placeholder stating that a password should be input
     * @param field
     * @param placeholder
     */
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
    protected void initializeComponents() {
        // Already initialized in constructor
    }

    /**
     * function setting up various listeners for fields and buttons in our project
     */
    @Override
    protected void setupListeners() {
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        createAccountButton.addActionListener(e -> showScene(App.CREATE_ACCOUNT));

        submitButton.addActionListener(e -> login());
    }

    /**
     * function that checks the validity of the username and password entered with what we have in the database
     */
    private void login() {
        final String uname = usernameField.getText();
        char[] pword = passwordField.getPassword();
        final String password = String.valueOf(pword);

        if (client.userLogin(uname, password)) {
            getApp().initScenes();
            showScene(App.GAME_LIBRARY);
        } else {
            JOptionPane.showMessageDialog(null, "Login Failed", null, JOptionPane.INFORMATION_MESSAGE);
        }
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