package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IconManager {

    public static void setIcon(JFrame frame, String iconPath) {
        try {
            Image iconImage = ImageIO.read(IconManager.class.getResource(iconPath));
            frame.setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
            // Puoi anche gestire il caso in cui l'immagine non venga trovata
        }
    }

    // Metodo per impostare l'icona a un JDialog
    public static void setIcon(JDialog dialog, String iconPath) {
        try {
            Image iconImage = ImageIO.read(IconManager.class.getResource(iconPath));
            dialog.setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
            // Puoi anche gestire il caso in cui l'immagine non venga trovata
        }
    }
}
