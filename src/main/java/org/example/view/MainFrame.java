package org.example.view;

import org.example.model.Book;
import org.example.model.BookDAO;
import org.example.model.CreateTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Calendar;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame extends JFrame {
    private JPanel bookPanel;
    private BookDAO bookDAO;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private String selectedMonth;
    private String selectedYear;
    public static final Color VERY_LIGHT_YELLOW = new Color(255,244,183);

    public MainFrame() {
        CreateTable.createNewTable();
        bookDAO = new BookDAO();
        setTitle("Book App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        IconManager.setIcon(this,"/book.png");

        Font defaultFont = new Font("Arial", Font.PLAIN, 14);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("TextArea.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        Image backgroundImage = null;
        try {
            URL backgroundImageUrl = getClass().getResource("/libreria.jpg");
            if (backgroundImageUrl != null) {
                backgroundImage = new ImageIcon(backgroundImageUrl).getImage();
            } else {
                System.err.println("Immagine di sfondo non trovata.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Layout principale
        // JPanel mainPanel = new JPanel(new BorderLayout());
        BackgroundPanel mainPanel = new BackgroundPanel(backgroundImage);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        comboPanel.setMaximumSize(new Dimension(800, 150));
        comboPanel.setOpaque(false);

        // ComboBox per selezionare il mese
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setPreferredSize(new Dimension(150, 30));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[3];
        for (int i = 0; i < 3; i++) {
            years[i] = Integer.toString(currentYear - i);
        }

        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        monthComboBox.setSelectedItem(months[currentMonth]);

        yearComboBox = new JComboBox<>(years);
        yearComboBox.setPreferredSize(new Dimension(100, 30));

        yearComboBox.setSelectedItem(currentYear);

        monthComboBox.addActionListener(new ComboBoxListener());
        yearComboBox.addActionListener(new ComboBoxListener());

        comboPanel.add(monthComboBox);
        comboPanel.add(yearComboBox);
        mainPanel.add(comboPanel);

        JButton showAllBooks = new JButton("Show all Books");
        showAllBooks.setPreferredSize(new Dimension(150, 30));
        showAllBooks.addActionListener(e-> loadAllBooks());
        comboPanel.add(showAllBooks);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        bookPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        bookPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(bookPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane);
        //mainPanel.add(new JScrollPane(bookPanel));

        add(mainPanel);



        loadBooksForMonth(months[currentMonth],years[0]);


        JButton addButton = new JButton("Add Book");
        mainPanel.add(addButton);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectedMonth = monthComboBox.getSelectedItem().toString();
        //System.out.println(selectedMonth);
        selectedYear = yearComboBox.getSelectedItem().toString();

        addButton.addActionListener(e -> {

            AddBookDialog addBookDialog = new AddBookDialog(MainFrame.this,selectedMonth,selectedYear);
            addBookDialog.setVisible(true);

            //public void actionPerformed(ActionEvent e) {
              //  EditBookDialog editBookDialog = new EditBookDialog(MainFrame.this,book,bookDAO,detailsFrame);
                //editBookDialog.setVisible(true);
        });



    }

    private class ComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedMonth = (String) monthComboBox.getSelectedItem();
            selectedYear = (String) yearComboBox.getSelectedItem();

            // Ensure both month and year are selected
            if (selectedMonth != null && selectedYear != null) {
                loadBooksForMonth(selectedMonth, (selectedYear));
            }
        }
    }


    public void loadBooksForMonth(String month,String year) {
        bookPanel.removeAll();
        List<Book> books = bookDAO.getBooksByMonth(month,year);

        for (Book book : books) {
            try {
                URL url = new URL(book.getCoverurl());
                ImageIcon coverIcon = new ImageIcon(url);
                Image scaledImage = coverIcon.getImage().getScaledInstance(100, 130, Image.SCALE_FAST);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                JLabel coverLabel = new JLabel(scaledIcon);
                coverLabel.setOpaque(false);

                String title = book.getTitle();
                if (title.length() > 15) {
                    title = title.substring(0, 12) + "...";
                }
                JButton titleButton = new JButton(title);
                titleButton.setOpaque(false);  // Imposta trasparenza del pulsante
                //titleButton.setContentAreaFilled(false);  // Rende il pulsante trasparente
                //titleButton.setBorderPainted(false);  // Rimuove il bordo del pulsante
                //titleButton.setFocusPainted(false);
                titleButton.addActionListener(e -> showBookDetails(book));

                //bookItemPanel.add(titleButton, BorderLayout.NORTH);
                //bookPanel.add(bookItemPanel);
                JPanel bookItemPanel = new JPanel();
                bookItemPanel.setLayout(new BoxLayout(bookItemPanel, BoxLayout.Y_AXIS));
                bookItemPanel.setOpaque(false);
                coverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                titleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                bookItemPanel.add(coverLabel);
                bookItemPanel.add(titleButton);
                bookPanel.add(bookItemPanel);
            }  catch (Exception e) {
                System.out.println("Errore nel caricamento dell'immagine: " + e.getMessage());
            }
        }

        bookPanel.revalidate();
        bookPanel.repaint();
    }

    // Mostra i dettagli del libro in una nuova finestra
    public void showBookDetails(Book book) {
        JFrame detailsFrame = new JFrame(book.getTitle());
        //detailsFrame.setBackground(VERY_LIGHT_YELLOW);
        detailsFrame.setSize(550, 500);
        detailsFrame.setLocationRelativeTo(this);
        //detailsFrame.setBackground(VERY_LIGHT_YELLOW);

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


        //JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // Utilizza BorderLayout per il layout principale
        //mainPanel.setBackground(VERY_LIGHT_YELLOW);
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.setBackground(new Color(255, 255, 255, 150));
        //detailsPanel.setBackground(VERY_LIGHT_YELLOW);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Aggiunge un margine al pannello dei dettagli
        detailsPanel.add(new JLabel("<html><b>Title: </b> " + book.getTitle() + "</html>"));
        detailsPanel.add(new JLabel("<html><b>Author: </b> " + book.getAuthor() + "</html>"));
        detailsPanel.add(new JLabel("<html><b>Rating: </b> " + book.getRating() + "</html>"));

        JTextArea reviewArea = new JTextArea(book.getReview());
        reviewArea.setLineWrap(true);  // Imposta il wrap per le linee troppo lunghe
        reviewArea.setWrapStyleWord(true);  // Spezza le linee sui confini delle parole
        reviewArea.setEditable(false);  // Rendi la JTextArea non modificabile

// Avvolgi la JTextArea in uno JScrollPane
        JScrollPane reviewScrollPane = new JScrollPane(reviewArea);
        reviewScrollPane.setMinimumSize(new Dimension(300, 200));  // Imposta una dimensione preferita

        detailsPanel.add(new JLabel("<html><b>Review: </b></html>"));  // Etichetta della recensione
        detailsPanel.add(reviewScrollPane);  // Aggiungi lo JScrollPane al pannello

        detailsPanel.add(new JLabel("<html><b>Month: </b> " + book.getMonth() + "</html>"));
        detailsPanel.add(new JLabel("<html><b>Year: </b> " + book.getYear() + "</html>"));

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel coverPanel = new JPanel(new BorderLayout());
        coverPanel.setMaximumSize(new Dimension(100, 130));
        coverPanel.setOpaque(false);
        try {
            URL url = new URL(book.getCoverurl());
            ImageIcon coverIcon = new ImageIcon(url);
            Image scaledImage = coverIcon.getImage().getScaledInstance(150, 190, Image.SCALE_FAST);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel coverLabel = new JLabel(scaledIcon);
            coverLabel.setOpaque(false);
            coverLabel.setMaximumSize(new Dimension(100, 130));
            coverPanel.add(coverLabel, BorderLayout.NORTH);
            coverPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));// Aggiungi la JLabel contenente l'immagine
        } catch (Exception e) {
            // Gestisci l'errore nel caso in cui non riesca a caricare l'immagine
            detailsPanel.add(new JLabel("Cover image not available"));
        }
        mainPanel.add(coverPanel, BorderLayout.EAST);

        detailsFrame.add(mainPanel);
        detailsFrame.setVisible(true);

        //EDIT ICON
        JButton editButton = new JButton(new ImageIcon(getClass().getResource("/edit_icon.png")));
        editButton.setBorderPainted(false);
        editButton.setContentAreaFilled(false);
        editButton.setFocusPainted(false);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditBookDialog editBookDialog = new EditBookDialog(MainFrame.this,book,bookDAO,detailsFrame);
                editBookDialog.setVisible(true);
            }
        });

        //DELETE ICON
        JButton deleteButton = new JButton(new ImageIcon(getClass().getResource("/bin.png")));
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Do you want to delete this book?", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    BookDAO bookDAO = new BookDAO();
                    bookDAO.deleteBook(book.getId());
                    //setVisible(false);
                    detailsFrame.dispose();
                    loadBooksForMonth(selectedMonth, (selectedYear));
                    // Azione se l'utente clicca su "Yes"
                }

                        //editBookDialog.setVisible(true);
            }
        });


        JPanel buttonPanel = new JPanel();

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);


        //detailsFrame.add(detailsPanel, BorderLayout.CENTER);
        detailsFrame.add(buttonPanel, BorderLayout.SOUTH);

        detailsFrame.setVisible(true);
    }

    // Metodo per mostrare tutti i libri
    private void loadAllBooks() {
        List<Book> allBooks = bookDAO.getAllBooks(); // Ottieni tutti i libri
        bookPanel.removeAll(); // Rimuovi i componenti esistenti nel pannello dei libri

        for (Book book : allBooks) {
            try {

                URL url = new URL(book.getCoverurl());
                ImageIcon coverIcon = new ImageIcon(url);
                Image scaledImage = coverIcon.getImage().getScaledInstance(100, 130, Image.SCALE_FAST);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                JLabel coverLabel = new JLabel(scaledIcon);
                coverLabel.setOpaque(false);

                String title = book.getTitle();
                if (title.length() > 15) {
                    title = title.substring(0, 12) + "...";
                }

                JButton titleButton = new JButton(title);
                titleButton.setOpaque(false);
                titleButton.addActionListener(e -> showBookDetails(book));


                JPanel bookItemPanel = new JPanel();
                bookItemPanel.setLayout(new BoxLayout(bookItemPanel, BoxLayout.Y_AXIS));
                bookItemPanel.setOpaque(false);
                coverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                titleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                bookItemPanel.add(coverLabel);
                bookItemPanel.add(titleButton);
                bookPanel.add(bookItemPanel);
            } catch (Exception e) {
                System.out.println("Errore nel caricamento dell'immagine: " + e.getMessage());
            }
        }


        bookPanel.revalidate();
        bookPanel.repaint();
    }



}