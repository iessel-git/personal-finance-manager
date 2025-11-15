package com.cse.financeapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.InputStream;

public class App {

    private static final String DB_URL = "jdbc:sqlite:finance.db";

    public static void main(String[] args) {

        System.out.println("Starting Finance App...");

        // Connect to SQLite
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            if (conn != null) {
                System.out.println("✔ Connected to SQLite database successfully!");
                System.out.println("Database file location: finance.db");

                // Initialize DB schema
                initializeDatabase(conn);
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to SQLite database");
            e.printStackTrace();
        }
    }

    private static void initializeDatabase(Connection conn) {
        try {
            // Load schema.sql from resources
            InputStream is = App.class.getResourceAsStream("/schema.sql");

            if (is == null) {
                System.out.println("❌ schema.sql NOT found in resources!");
                return;
            }

            // Convert the file into a String
            String schema = new String(is.readAllBytes());

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(schema);
                System.out.println("✔ Database schema initialized successfully!");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to initialize database schema");
            e.printStackTrace();
        }
    }
}
