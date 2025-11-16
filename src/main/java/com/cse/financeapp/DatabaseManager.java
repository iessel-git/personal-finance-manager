package com.cse.financeapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.InputStream;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:data/finance.db";


    /**
     * Initializes:
     * - Loads SQLite JDBC driver
     * - Initializes schema.sql
     */
    public void initialize() {
        loadDriver();
        initSchema();
    }

    /**
     * Load SQLite driver
     */
    private void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✔ SQLite JDBC Driver loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ SQLite JDBC Driver NOT found!");
            e.printStackTrace();
        }
    }

    /**
     * Execute schema.sql from resources
     */
    private void initSchema() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            if (conn == null) {
                System.out.println("❌ Could not establish DB connection for schema init");
                return;
            }

            InputStream is = DatabaseManager.class.getResourceAsStream("/schema.sql");
            if (is == null) {
                System.out.println("❌ schema.sql NOT found in resources!");
                return;
            }

            String schema = new String(is.readAllBytes());

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(schema);
                System.out.println("✔ Database schema initialized");
            }

        } catch (Exception e) {
            System.out.println("❌ Error while initializing schema");
            e.printStackTrace();
        }
    }

    /**
     * Provides DB connection to DAO classes
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
