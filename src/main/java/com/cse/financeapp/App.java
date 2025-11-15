package com.cse.financeapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    private static final String DB_URL = "jdbc:sqlite:finance.db";

    public static void main(String[] args) {

        System.out.println("Starting Finance App...");

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            if (conn != null) {
                System.out.println("✔ Connected to SQLite database successfully!");
                System.out.println("Database file location: finance.db");
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to SQLite database");
            e.printStackTrace();
        }
    }
}
