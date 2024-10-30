package org.example.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DropTable {
    public static void dropBooksTable() {
        String sql = "DROP TABLE IF EXISTS books";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            // Esecuzione della query per cancellare la tabella
            stmt.execute(sql);
            System.out.println("Table `books` deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        dropBooksTable();
    }
}
