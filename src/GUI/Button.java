package GUI;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Deprecated
public class Button {
    private final JButton button;
    private final JLabel buttonImg;
    private final int buttonW;
    private final int buttonH;

    private final Clip clip;
    Button(int buttonX, int buttonY, int buttonW, int buttonH, double scale, String img1, String img2){
        this.buttonW = (int)(buttonW*scale);
        this.buttonH = (int)(buttonH*scale);

        try {
            // Load audio clip
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Button.class.getResourceAsStream("Sound Effect//Click.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        this.button = new JButton();
        this.button.setOpaque(false);
        this.button.setContentAreaFilled(false);
        this.button.setBorderPainted(false);
        this.button.setBounds(buttonX,buttonY,this.buttonW,this.buttonH);

        this.buttonImg = new JLabel();
        this.buttonImg.setBounds(buttonX,buttonY,this.buttonW,this.buttonH);
        this.buttonImg.setIcon(scaleImage(img1));
        this.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                buttonImg.setIcon(scaleImage(img2));
                buttonImg.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonImg.setIcon(scaleImage(img1));
                buttonImg.repaint();
                playSound();
            }
        });
    }

    public JButton button(){
        return this.button;
    }

    public JLabel buttonImg(){
        return this.buttonImg;
    }

    public ImageIcon scaleImage(String imgPath) {
        ImageIcon imageIcon = new ImageIcon(imgPath);
        Image image = imageIcon.getImage();
        Image newImg = image.getScaledInstance(this.buttonW,this.buttonH,Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    public void playSound() {
        if (clip != null) {
            // Stop the clip if it's already running
            if (clip.isRunning())
                clip.stop();

            // Rewind the clip to the beginning
            clip.setFramePosition(0);

            // Start playing the clip
            clip.start();
        }
    }
}

