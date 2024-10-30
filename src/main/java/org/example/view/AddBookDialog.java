package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.net.MalformedURLException;
import java.net.URL;

import org.example.model.BookDAO;
import org.example.model.Book;
import org.example.model.BookCoverFetcher;

public class AddBookDialog extends JDialog {

    private JTextField titleField;
    private JTextField authorField;
    private JSpinner ratingField;
    private JTextField reviewField;
    private JTextField coverUrlField;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;

    public  AddBookDialog(MainFrame parent,String selectedMonth,String selectedYear) {
        super(parent, "Add Book", true);

        setSize(400, 510);
        setLocationRelativeTo(parent);

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

        titleField = new JTextField();
        authorField = new JTextField();
        ratingField = new JSpinner( new SpinnerNumberModel(1.0,1.0,5.0,0.5));
        reviewField = new JTextField();
        coverUrlField = new JTextField();
        monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"// etc...
        });
        monthComboBox.setSelectedItem(selectedMonth);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[3];
        for (int i = 0; i < 3; i++) {
            years[i] = Integer.toString(currentYear - i);
        }
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedItem(selectedYear);

        editPanel.add(new JLabel("Title:")).setFont(boldFont);
        editPanel.add(titleField);
        editPanel.add(new JLabel("Author:")).setFont(boldFont);
        editPanel.add(authorField);
        editPanel.add(new JLabel("Rating:")).setFont(boldFont);
        editPanel.add(ratingField);
        editPanel.add(new JLabel("Review:")).setFont(boldFont);
        editPanel.add(reviewField);
        editPanel.add(new JLabel("Cover URL:")).setFont(boldFont);
        editPanel.add(coverUrlField);
        //editPanel.add(new JScrollPane(reviewField));
        editPanel.add(new JLabel("Month:")).setFont(boldFont);
        editPanel.add(monthComboBox);
        editPanel.add(new JLabel("Year:")).setFont(boldFont);
        editPanel.add(yearComboBox);


        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
                dispose();

            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.setOpaque(false);
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(editPanel,BorderLayout.CENTER);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);
        setVisible(true);


    }

    private void addBook() {

        String title = titleField.getText();
        String author = authorField.getText();
        double rating = (double) ratingField.getValue();
        String review = reviewField.getText();
        String month = (String) monthComboBox.getSelectedItem();
        String year = (String) yearComboBox.getSelectedItem();
        String coverurl = coverUrlField.getText();

        if (title.isEmpty() || month == null || year == null ) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields (Title, Month, Year).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (coverurl.isEmpty()){
            BookCoverFetcher bookCoverFetcher = new BookCoverFetcher();
            String fetchedurl = bookCoverFetcher.getBookCoverURL(title,author);
            coverurl = fetchedurl;
            System.out.println(fetchedurl);

            if (!isValidURL(coverurl)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid URL", "Invalid URL", JOptionPane.ERROR_MESSAGE);
                return;
            }

        }




        BookDAO bookDAO = new BookDAO();
        Book book = new Book(title, author, rating,month,year,coverurl,review);
        bookDAO.addBook(book);


        //setVisible(false);
        ((MainFrame) getParent()).loadBooksForMonth(month,year);


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

