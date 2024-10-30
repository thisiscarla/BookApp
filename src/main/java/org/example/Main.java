package org.example;

//import org.example.controller.BookController;
import org.example.view.MainFrame;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;


public class Main {
    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}











