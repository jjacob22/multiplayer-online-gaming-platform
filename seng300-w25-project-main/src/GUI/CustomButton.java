package GUI;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    private ImageIcon sprite;
    private static final int SHRINK = 10;

    public CustomButton() {
        super();
        setFocusable(false);
        setRolloverEnabled(false);
        setFocusPainted(false);
        setBorder(null);
        sprite = null;
    }

    CustomButton(ImageIcon sprite) {
        super();
        setFocusable(false);
        setRolloverEnabled(false);
        setFocusPainted(false);
        setBorder(null);
        this.sprite = sprite;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sprite != null) {
            g.drawImage(sprite.getImage(), SHRINK/2, SHRINK/2, getWidth() - SHRINK, getHeight() - SHRINK, this);
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
