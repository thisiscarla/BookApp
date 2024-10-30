package org.example.view;
import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegna l'immagine di sfondo scalata alla dimensione del pannello
        if (backgroundImage != null) {
            //System.out.println("Background image trovata:"+backgroundImage.getWidth(null));
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    }