package org.example.view;

import org.example.model.Book;
import org.example.model.BookCoverFetcher;
import org.example.model.BookDAO;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class EditBookDialog extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JSpinner ratingSpinner;
    private JTextArea reviewArea;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JTextField coverUrlField;

    private Book book;
    private BookDAO bookDAO;
    private JFrame detailsFrame;
    private MainFrame mainFrame;  // Riferimento al MainFrame per ricaricare i dati

    public EditBookDialog(MainFrame mainFrame, Book book, BookDAO bookDAO,JFrame detailsFrame) {
        super(mainFrame, "Edit Book", true);
        this.book = book;
        this.bookDAO = bookDAO;
        this.detailsFrame = detailsFrame;
        this.mainFrame = mainFrame;

        setSize(400, 510);
        setLocationRelativeTo(mainFrame);

        Font boldFont = new Font("Arial", Font.BOLD, 14);
        Image backgroundImageDetails = null;
        try {
            URL backgroundImageDetailsUrl = getClass().getResource("/wood2.jpg");
            if (backgroundImageDetailsUrl != null) {
                backgroundImageDetails = new ImageIcon(backgroundImageDetailsUrl).getImage();
            } else {
                System.err.println("Immagine di sfondo dettagli non trovata.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BackgroundPanel mainPanel = new BackgroundPanel(backgroundImageDetails);
        mainPanel.setLayout(new BorderLayout(10, 10));
        JPanel editPanel = new JPanel(new GridLayout(8, 2,0,10));
        editPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        editPanel.setBackground(new Color(255, 255, 255, 150));

        // Campi di input per i dettagli del libro
        titleField = new JTextField(book.getTitle());
        //System.out.println(book.getId()+book.getTitle());
        authorField = new JTextField(book.getAuthor());
        ratingSpinner = new JSpinner(new SpinnerNumberModel(book.getRating(), 1.0, 5.0, 0.5));
        reviewArea = new JTextArea(book.getReview());
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"// etc...
        });
        monthComboBox.setSelectedItem(book.getMonth());

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[3];  // Anni da 10 anni fa fino all'anno corrente
        for (int i = 0; i < 3; i++) {
            years[i] = Integer.toString(currentYear - i);
        }
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedItem(book.getYear());
        coverUrlField = new JTextField(book.getCoverurl());

        // Aggiunta dei campi al pannello
        editPanel.add(new JLabel("Title:")).setFont(boldFont);
        editPanel.add(titleField);
        editPanel.add(new JLabel("Author:")).setFont(boldFont);
        editPanel.add(authorField);
        editPanel.add(new JLabel("Rating:")).setFont(boldFont);
        editPanel.add(ratingSpinner);
        editPanel.add(new JLabel("Review:")).setFont(boldFont);
        editPanel.add(new JScrollPane(reviewArea)); // ScrollPane per la recensione
        editPanel.add(new JLabel("Month:")).setFont(boldFont);
        editPanel.add(monthComboBox);
        editPanel.add(new JLabel("Year:")).setFont(boldFont);
        editPanel.add(yearComboBox);
        editPanel.add(new JLabel("Cover URL:")).setFont(boldFont);
        editPanel.add(coverUrlField);

        // Pulsante per salvare le modifiche
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBook();
                if (detailsFrame != null) {
                    detailsFrame.dispose(); // Chiudi la finestra dei dettagli
                }

            }
        });

        // Pannello con pulsante "Save" centrato
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        //editPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        // Aggiunta di componenti al dialog
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(editPanel,BorderLayout.CENTER);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);
        //add(editPanel, BorderLayout.CENTER);
        //add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Metodo per salvare le modifiche del libro
    private void saveBook() {
        try {
            // Aggiorna i dettagli del libro
            System.out.println(book.getId());
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setRating((Double) ratingSpinner.getValue());
            book.setReview(reviewArea.getText());
            String month = (String) monthComboBox.getSelectedItem();
            String year = (String) yearComboBox.getSelectedItem();
            //book.setCoverurl(coverUrlField.getText());
            String coverurl = coverUrlField.getText();

            if (book.getTitle().isEmpty() || month == null || year == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields (Title, Month, Year).", "Input Error", JOptionPane.ERROR_MESSAGE);
                return; // Non proseguire se ci sono errori
            }

            if (coverurl.isEmpty()){
                BookCoverFetcher bookCoverFetcher = new BookCoverFetcher();
                String fetchedurl = bookCoverFetcher.getBookCoverURL(titleField.getText(),authorField.getText());
                coverurl = fetchedurl;
                //System.out.println(fetchedurl);
                book.setCoverurl(coverurl);
            }
            else{
                book.setCoverurl(coverUrlField.getText());
            };


            if (!isValidURL(coverurl)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid URL", "Invalid URL", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Salva le modifiche nel database
            bookDAO.updateBook(book);

            // Ricarica i libri del MainFrame per il mese e anno selezionati
            ((MainFrame) getParent()).loadBooksForMonth(month,year);
            ((MainFrame) getParent()).showBookDetails(book);


            // Chiudi il dialog
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving book details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public boolean isValidURL(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
