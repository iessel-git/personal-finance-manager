package com.cse.financeapp.repository;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.service.SupabaseClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class CategoryRepository {

    private final SupabaseClient client;
    private final Gson gson = new Gson();

    public CategoryRepository(SupabaseClient client) {
        this.client = client;
    }

    // Add a category
    public void addCategory(Category category) throws Exception {
        String jsonBody = gson.toJson(category);
        client.insert("categories", jsonBody);
    }

    // Get all categories
    public List<Category> getAllCategories() throws Exception {
        String json = client.select("categories");
        return gson.fromJson(json, new TypeToken<List<Category>>(){}.getType());
    }

    // Delete category by ID
    public void deleteCategory(int id) throws Exception {
        client.delete("categories", id);
    }
}
