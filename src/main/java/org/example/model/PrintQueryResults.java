package org.example.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PrintQueryResults {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:books.db";


        String sql = "SELECT * FROM books";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            while (rs.next()) {

                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                double rating = rs.getDouble("rating");
                String review = rs.getString("review");
                String month = rs.getString("month");
                String year = rs.getString("year");
                String coverUrl = rs.getString("coverurl");


                System.out.println("ID: " + id);
                System.out.println("Title: " + title);
                System.out.println("Author: " + author);
                System.out.println("Rating: " + rating);
                System.out.println("Review: " + review);
                System.out.println("Month: " + month);
                System.out.println("Year: " + year);
                System.out.println("Cover URL: " + coverUrl);
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
