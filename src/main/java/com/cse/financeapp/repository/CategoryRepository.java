package com.cse.financeapp.repository;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private final SupabaseClient client;

    public CategoryRepository(SupabaseClient client) {
        this.client = client;
    }

    // Add new category
    public void addCategory(Category category) throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", category.getName());
        json.put("description", category.getDescription());
        client.insert("categories", json.toString());
    }

    // Get all categories
    public List<Category> getCategories() throws Exception {
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

    // Update category
    public void updateCategory(int id, Category category) throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", category.getName());
        json.put("description", category.getDescription());
        client.update("categories?id=eq." + id, json.toString());
    }

    // Delete category
    public void deleteCategory(int id) throws Exception {
        client.delete("categories?id=eq." + id);
    }
}
