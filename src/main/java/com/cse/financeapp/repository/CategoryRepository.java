package com.cse.financeapp.repository;

import com.cse.financeapp.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private final Connection conn;

    public CategoryRepository(Connection conn) {
        this.conn = conn;
    }

    // Insert new category
    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category (name, description) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.executeUpdate();
        }
    }

    // Retrieve all categories
    public List<Category> getAllCategories() throws SQLException {
        String sql = "SELECT id, name, description FROM category ORDER BY id ASC";
        List<Category> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        }
        return list;
    }

    // Update category
    public void updateCategory(int id, Category category) throws SQLException {
        String sql = "UPDATE category SET name = ?, description = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    // Delete category
    public void deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM category WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
