package com.cse.financeapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private final DatabaseManager dbManager;

    public ExpenseDAO() {
        this.dbManager = new DatabaseManager();
    }

    // -------------------------------------------------------
    // Add Expense (PostgreSQL + Supabase)
    // -------------------------------------------------------
    public void addExpense(Expense expense) {

        String sql = """
            INSERT INTO expenses (description, amount, date, category_id, note)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getDate());     // You may later convert to java.sql.Date
            stmt.setInt(4, expense.getCategoryId());  // FK
            stmt.setString(5, expense.getNote());

            stmt.executeUpdate();
            System.out.println("✔ Expense added!");

        } catch (SQLException e) {
            System.out.println("❌ Failed to add expense");
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------
    // Get All Expenses (PostgreSQL)
    // -------------------------------------------------------
    public List<Expense> getExpenses() {
        List<Expense> list = new ArrayList<>();

        String sql = """
            SELECT e.id, e.description, e.amount, e.date, e.note,
                   c.id AS category_id, c.name AS category_name
            FROM expenses e
            LEFT JOIN categories c ON e.category_id = c.id
            ORDER BY e.date DESC
        """;

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Expense exp = new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getString("date"),
                        rs.getString("note"),
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                );

                list.add(exp);
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch expenses");
            e.printStackTrace();
        }

        return list;
    }

    // -------------------------------------------------------
    // Delete Expense
    // -------------------------------------------------------
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

