package GUI;

import networking.Client;

import javax.swing.*;
import java.awt.*;

@Deprecated
public class LoadingScene extends Scene {
    public LoadingScene(App app) {
        super(app);
    }

    @Override
    protected void initializeComponents() {
        ImageIcon loadingIcon = new ImageIcon();
        JLabel loadingLabel = new JLabel(loadingIcon);
        add(loadingLabel, BorderLayout.CENTER);
    }

    @Override
    protected void resizeComponents() {

    }

    @Override
    protected void updateScene() {

    }

    @Override
    protected void setupListeners() {

    }

    @Override
    protected void assignImages() {

    }
}
