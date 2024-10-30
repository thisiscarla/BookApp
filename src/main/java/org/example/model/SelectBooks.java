package org.example.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectBooks {

    public static void selectAll(){
        String sql = "SELECT id, title, author, rating, review, month, year, coverurl FROM books";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){


            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("title") + "\t" +
                        rs.getString("author") + "\t" +
                        rs.getInt("rating") + "\t" +
                        rs.getString("review") + "\t" +
                        rs.getString("month") + "\t" +
                        rs.getString("cover_url"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        selectAll();
    }
}

