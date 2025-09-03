package GUI;

import javax.swing.*;
import java.awt.*;

public class RoundButton extends JButton {
    private ImageIcon sprite;

    public RoundButton() {
        super();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFocusable(false);
        setBorderPainted(false);
        sprite = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(CustomColor.BLUE_1);
        // Draw a round button
        g.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        if (sprite != null) {
            g.drawImage(sprite.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void setSprite(ImageIcon sprite) {
        this.sprite = sprite;
        repaint();
    }

    public ImageIcon getSprite() {
        return sprite;
    }
}
