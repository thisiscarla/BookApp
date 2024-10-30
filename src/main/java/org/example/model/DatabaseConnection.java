package org.example.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connect() {
        Connection conn = null;
        try {

            String url = "jdbc:sqlite:books.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connessione al database SQLite stabilita.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
