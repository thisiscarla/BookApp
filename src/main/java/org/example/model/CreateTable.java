package org.example.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void createNewTable() {

        String sql = "CREATE TABLE IF NOT EXISTS books (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	title text NOT NULL,\n"
                + "	author text NOT NULL,\n"
                + "	rating double,\n"
                + "	review text,\n"
                + "	month text NOT NULL,\n"
                + "	year text NOT NULL,\n"
                + "	coverurl text\n"
                + ");";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabella `books` creata.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        createNewTable();
    }
}

