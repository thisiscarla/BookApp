package org.example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertBook {

    public static void insert(String title, String author, int rating, String review, String month,String year, String coverurl) {
        String sql = "INSERT INTO books(title, author, rating, review, month,year, coverurl) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, rating);
            pstmt.setString(4, review);
            pstmt.setString(5, month);
            pstmt.setString(6, year);
            pstmt.setString(7, coverurl);
            pstmt.executeUpdate();
            System.out.println("Book added in DB.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        for (int i =0; i<7;i++) {
            insert("Harry Potter", "JKRownling", 5, "Great fantasy book", "September", "2024", "https://images.booksense.com/images/700/596/9781338596700.jpg");
        }
        }
}

