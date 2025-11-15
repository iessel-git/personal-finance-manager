package com.cse.financeapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private final DatabaseManager dbManager;

    public ExpenseDAO() {
        this.dbManager = new DatabaseManager();
    }

    // Add expense
    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (category, amount, date, note) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, expense.getCategory());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getDate());
            stmt.setString(4, expense.getNote());

            stmt.executeUpdate();
            System.out.println("✔ Expense added!");

        } catch (SQLException e) {
            System.out.println("❌ Failed to add expense");
            e.printStackTrace();
        }
    }

    // Get all expenses
    public List<Expense> getExpenses() {
        List<Expense> list = new ArrayList<>();

        String sql = "SELECT * FROM expenses ORDER BY date DESC";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("date"),
                        rs.getString("note")
                ));
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch expenses");
            e.printStackTrace();
        }

        return list;
    }

    // Delete expense
    public void deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✔ Expense deleted!");

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete expense");
            e.printStackTrace();
        }
    }
}
