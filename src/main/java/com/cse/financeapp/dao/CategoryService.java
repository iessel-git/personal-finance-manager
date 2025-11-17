package com.cse.financeapp.dao;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryService - safe CRUD using SupabaseClient
 */
public class CategoryService {

    private final SupabaseClient client;

    public CategoryService(SupabaseClient client) {
        this.client = client;
    }

    // create category, returns created id
    public int createCategory(String name, String description) throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("description", description);

        String resp = client.insert("categories", json.toString());
        // If Supabase returns an array with created rows, parse id
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            if (arr.isEmpty()) throw new RuntimeException("Insert returned empty array");
            return arr.getJSONObject(0).getInt("id");
        } else {
            // error object returned
            throw new RuntimeException("Supabase error on createCategory: " + resp);
        }
    }

    // list categories mapped to models
    public List<Category> listCategories() throws Exception {
        String resp = client.select("categories");
        List<Category> out = new ArrayList<>();
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                out.add(new Category(
                        o.getInt("id"),
                        o.optString("name", null),
                        o.optString("description", null)
                ));
            }
            return out;
        } else {
            throw new RuntimeException("Supabase error on listCategories: " + resp);
        }
    }

    // safe update: only non-null fields are sent via PATCH
    public boolean updateCategory(int id, String newName, String newDescription) throws Exception {
        JSONObject json = new JSONObject();
        if (newName != null) json.put("name", newName);
        if (newDescription != null) json.put("description", newDescription);

        if (json.isEmpty()) throw new IllegalArgumentException("No fields provided to updateCategory");

        String resp = client.patch("categories", id, json.toString());
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            throw new RuntimeException("Supabase error on updateCategory: " + resp);
        }
    }

    // delete by id
    public boolean deleteCategory(int id) throws Exception {
        String resp = client.delete("categories", id);
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            // Could be empty array or error object — treat object as error
            throw new RuntimeException("Supabase error on deleteCategory: " + resp);
        }
    }

    private boolean isArray(String raw) {
        if (raw == null) return false;
        String t = raw.trim();
        return t.startsWith("[");
    }
}
