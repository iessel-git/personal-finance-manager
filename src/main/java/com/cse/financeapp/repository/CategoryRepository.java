package com.cse.financeapp.repository;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.service.SupabaseClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class CategoryRepository {
    private final SupabaseClient client;

    public CategoryRepository(SupabaseClient client) {
        this.client = client;
    }

    // Add new category
    public void addCategory(Category category) throws Exception {
        String json = new JSONObject()
                .put("name", category.getName())
                .put("description", category.getDescription())
                .toString();

        client.insert("categories", json);
    }

    // Get all categories
    public List<Category> getAllCategories() throws Exception {
        List<Category> list = new ArrayList<>();
        String response = client.select("categories");

        JSONArray arr = new JSONArray(response);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            list.add(new Category(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.optString("description", "")
            ));
        }

        return list;
    }

    // Delete category by ID
    public void deleteCategory(int id) throws Exception {
        client.delete("categories", id);
    }

    // Update category
    public void updateCategory(int id, Category category) throws Exception {
        String json = new JSONObject()
                .put("name", category.getName())
                .put("description", category.getDescription())
                .toString();

        // Supabase REST API requires PATCH via POST with method override
        String url = "categories?id=eq." + id;
        client.update(url, json); // you'll need to add an 'update' method in SupabaseClient
    }
}
