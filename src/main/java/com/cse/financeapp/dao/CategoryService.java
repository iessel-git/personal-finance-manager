package com.cse.financeapp.dao;

import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryService — Version B using SupabaseClient.
 * This version uses PATCH for updates so missing fields are not overwritten as null.
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
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on createCategory: " + o);
        }
    }

    // List all categories
    public List<JSONObject> listRawCategories() throws Exception {
        String resp = client.select("categories");
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            List<JSONObject> out = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                out.add(arr.getJSONObject(i));
            }
            return out;
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on select categories: " + o);
        }
    }

    /**
     * Update category safely using PATCH.
     * Only non-null fields are sent to Supabase.
     */
    public boolean updateCategory(int id, String newName, String newDescription) throws Exception {
        JSONObject json = new JSONObject();

        if (newName != null) json.put("name", newName);
        if (newDescription != null) json.put("description", newDescription);

        if (json.isEmpty()) {
            throw new RuntimeException("No fields provided to updateCategory");
        }

        String resp = client.patch("categories", id, json.toString());

        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on updateCategory: " + o);
        }
    }

    // Delete by ID
    public boolean deleteCategory(int id) throws Exception {
        String resp = client.delete("categories", id);
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on deleteCategory: " + o);
        }
    }

    // Helper
    private boolean isArray(String raw) {
        if (raw == null) return false;
        String t = raw.trim();
        return t.startsWith("[");
    }
}
