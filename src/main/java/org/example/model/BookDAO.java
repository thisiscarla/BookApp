package org.example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class BookDAO {

    public void addBook(Book book) {
        String sql = "INSERT INTO books(title, author, rating, review, month,year, coverurl) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setDouble(3, book.getRating());
            pstmt.setString(4, book.getReview());
            pstmt.setString(5, book.getMonth());
            pstmt.setString(6, book.getYear());
            pstmt.setString(7, book.getCoverurl());
            pstmt.executeUpdate();
            System.out.println("Libro inserito nel database.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("rating"),
                        rs.getString("month"),
                        rs.getString("year"),
                        rs.getString("coverurl"),
                        rs.getString("review")
                );
                books.add(book);
                book.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    public List<Book> getBooksByMonth(String month, String year) {
        String sql = "SELECT * FROM books WHERE month = ? AND year = ? ";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, month);
            pstmt.setString(2, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("rating"),
                        rs.getString("month"),
                        rs.getString("year"),
                        rs.getString("coverurl"),
                        rs.getString("review")
                );
                book.setId(rs.getInt("id"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, rating = ?, review = ?, month = ?,year = ?, coverurl = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("Updating book with ID: " + book.getId());


            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setDouble(3, book.getRating());
            pstmt.setString(4, book.getReview());
            pstmt.setString(5, book.getMonth());
            pstmt.setString(6, book.getYear());
            pstmt.setString(7, book.getCoverurl());
            pstmt.setInt(8, book.getId());

            int affectedRows = pstmt.executeUpdate();
            System.out.println(affectedRows);


            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setInt(1, bookId);


            int affectedRows = pstmt.executeUpdate();


            if (affectedRows > 0) {
                System.out.println("Book with ID " + bookId + " has been deleted successfully.");
            } else {
                System.out.println("No book found with ID " + bookId);
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
