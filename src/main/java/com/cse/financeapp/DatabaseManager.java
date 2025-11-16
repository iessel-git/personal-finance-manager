package com.cse.financeapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:postgresql://db.ggjvorvnrrbqixszpemk.supabase.co:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "@KasoaTemaOlebu12$";

    public DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✔ PostgreSQL JDBC Driver loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ PostgreSQL JDBC Driver NOT found");
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void initialize() {
        System.out.println("✔ Connected to Supabase PostgreSQL");
        System.out.println("⚠️  Note: Schema created manually in Supabase SQL Editor");
    }
}


