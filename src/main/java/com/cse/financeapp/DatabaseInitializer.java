package com.cse.financeapp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/finance.db";
    private static final String SCHEMA_PATH = "src/main/resources/schema.sql";

    public static void initialize() {
        try {
            // Read schema.sql content
            String schema = new String(Files.readAllBytes(Paths.get(SCHEMA_PATH)));

            // Connect to SQLite
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();

            // Execute schema
            stmt.executeUpdate(schema);

            System.out.println("Database initialized successfully!");

            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
