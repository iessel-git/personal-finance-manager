package com.cse.financeapp.dao;

import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryService — uses SupabaseClient (Version B).
 * It inspects raw responses: if response starts with '[' it's an array (success),
 * otherwise it treats response as error object and throws with details.
 */
public class CategoryService {

    private final SupabaseClient client;

    public CategoryService(SupabaseClient client) {
        this.client = client;
    }

    // Create category. Returns created ID.
    public int createCategory(String name, String description) throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("description", description);

        String resp = client.insert("categories", json.toString());
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            if (arr.isEmpty()) throw new RuntimeException("Insert returned empty array");
            return arr.getJSONObject(0).getInt("id");
        } else {
            // error object
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on createCategory: " + o.toString());
        }
    }

    // List all categories
    public List<JSONObject> listRawCategories() throws Exception {
        String resp = client.select("categories");
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            List<JSONObject> out = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) out.add(arr.getJSONObject(i));
            return out;
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on select categories: " + o.toString());
        }
    }

    // Update category (returns true if updated)
    public boolean updateCategory(int id, String newDescription) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("description", newDescription);

        String resp = client.upsert("categories", json.toString());
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on updateCategory: " + o.toString());
        }
    }

    // Delete by id
    public boolean deleteCategory(int id) throws Exception {
        String resp = client.delete("categories", id);
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            JSONObject o = new JSONObject(resp);
            // If file is empty array due to not found, server may return []
            // But if object, it's an error
            throw new RuntimeException("Supabase error on deleteCategory: " + o.toString());
        }
    }

    // Helper
    private boolean isArray(String raw) {
        if (raw == null) return false;
        String t = raw.trim();
        return t.startsWith("[");
    }
}
