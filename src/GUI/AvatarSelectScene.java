package GUI;

import networking.Client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Displays UI components for selecting an avatar.
 */
public class AvatarSelectScene extends Scene {
    public final String AVATARS_PATH = "avatars/";
    public final String AVATAR_EXTENSION = ".png";

    private final JPanel avatarsPanel = new JPanel();
    private final JButton avatarButton;
    private final ArrayList<String> avatarsNames;
    private JScrollPane avatarsScrollPane = new JScrollPane();
    private JFrame avatarSelectFrame;

    public AvatarSelectScene(JFrame frame, App app, ArrayList<String> avatarsNames, JButton avatarButton) {
        super(app);

        this.avatarButton = avatarButton;
        this.avatarsNames = avatarsNames;
        avatarSelectFrame = frame;
        avatarSelectFrame.add(this);

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

        avatarsPanel.setBackground(CustomColor.BLUE_1);
        avatarsPanel.setLayout(new GridLayout(0, 4, (int) (this.getWidth() * 0.01), (int) (this.getWidth() * 0.01)));

        for (String name: avatarsNames) {
            JButton gameButton = createButton(name);
            avatarsPanel.add(gameButton);
        }

        avatarsScrollPane = new JScrollPane(avatarsPanel);
        avatarsScrollPane.setBackground(CustomColor.BLUE_2);
        avatarsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        avatarsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        avatarsScrollPane.setBorder(new BevelBorder(1, CustomColor.DARK_BLUE_2, CustomColor.DARK_BLUE_1));
        editUIScrollPane(avatarsScrollPane);

        add(avatarsScrollPane, BorderLayout.CENTER);
    }

    /**
     * Assigns images to components.
     */
    @Override
    protected void assignImages() {
        try {
            for (Component component : avatarsPanel.getComponents()) {
                JButton button = (JButton) component;
                button.setIcon(getButtonImage( AVATARS_PATH + component.getName() + AVATAR_EXTENSION, button.getPreferredSize().width, button.getPreferredSize().height));
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
        int numCols = 4;
        int buttonSize = (int) ((double) this.getWidth() / numCols - avatarsScrollPane.getVerticalScrollBar().getWidth());
        for (Component button : avatarsPanel.getComponents()) {
            if (button instanceof JButton) {
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                button.revalidate();
            }
        }

        avatarsPanel.setBorder(BorderFactory.createEmptyBorder((int) (this.getHeight() * 0.01), (int) (this.getWidth() * 0.01), (int) (this.getHeight() * 0.10), 0));
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
        int i = 0;
        for (Component component : avatarsPanel.getComponents()) {
            JButton button = (JButton) component;

            int finalI = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Assign to numbers for client
                    client.updateAvatar(finalI);
                    avatarButton.setIcon(getButtonImage(AVATARS_PATH + avatarsNames.get(client.getAvatar()) + AVATAR_EXTENSION, avatarButton.getWidth(), avatarButton.getHeight()));
                }
            });
            i++;
        }
    }

}

