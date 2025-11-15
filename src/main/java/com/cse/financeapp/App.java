package com.cse.financeapp;

public class App {

    public static void main(String[] args) {

        System.out.println("Starting Finance App...");

        // -------------------------------------
        // Initialize Database using DatabaseManager
        // -------------------------------------
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.initialize();  // loads JDBC driver + initializes schema.sql

        // -------------------------------------
        // Test DAO operations
        // -------------------------------------
        ExpenseDAO dao = new ExpenseDAO();

        // Test: Add an expense
        dao.addExpense(new Expense(
                "Food",
                12.50,
                "2025-11-14",
                "Lunch with team"
        ));

        // Test: Fetch and print all expenses
        System.out.println("\nAll expenses:");
        dao.getExpenses().forEach(System.out::println);

        System.out.println("\nFinance App finished.");
        System.out.println("DB Path: " + new java.io.File("finance.db").getAbsolutePath());

    }
}
