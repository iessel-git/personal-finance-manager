package com.cse.financeapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:finance.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite Driver loaded.");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load SQLite JDBC driver!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
