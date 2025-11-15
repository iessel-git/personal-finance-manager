package com.cse.financeapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    // CREATE
    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (category, amount, date, note) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, expense.getCategory());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getDate());
            pstmt.setString(4, expense.getNote());

            pstmt.executeUpdate();
            System.out.println("✔ Expense added: " + expense);

        } catch (SQLException e) {
            System.out.println("❌ Failed to add expense");
            e.printStackTrace();
        }
    }

    // READ
    public List<Expense> getExpenses() {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses";

        try (Connection conn = DatabaseManager.getConnection();
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

    // DELETE
    public void deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            System.out.println("✔ Expense deleted (ID: " + id + ")");

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete expense");
            e.printStackTrace();
        }
    }
}
