package com.cse.financeapp.dao;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple CategoryService (Option A).
 */
public class CategoryService {
    private final SupabaseClient client;

    public CategoryService(SupabaseClient client) {
        this.client = client;
    }

    // create, returns id
    public int createCategory(String name, String description) throws Exception {
        JSONObject j = new JSONObject();
        j.put("name", name);
        j.put("description", description);
        String resp = client.insert("categories", j.toString());
        JSONArray arr = new JSONArray(resp);
        if (arr.isEmpty()) throw new RuntimeException("Category insert returned empty");
        return arr.getJSONObject(0).getInt("id");
    }

    public List<Category> listCategories() throws Exception {
        String resp = client.select("categories");
        JSONArray arr = new JSONArray(resp);
        List<Category> out = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            out.add(new Category(
                    o.optInt("id", -1),
                    o.optString("name", null),
                    o.optString("description", null)
            ));
        }
        return out;
    }

    public boolean updateCategory(int id, String name, String description) throws Exception {
        JSONObject j = new JSONObject();
        if (name != null) j.put("name", name);
        if (description != null) j.put("description", description);
        String resp = client.update("categories", id, j.toString());
        JSONArray arr = new JSONArray(resp);
        return !arr.isEmpty();
    }

    public boolean deleteCategory(int id) throws Exception {
        String resp = client.delete("categories", id);
        JSONArray arr = new JSONArray(resp);
        // Supabase returns [] on deletion; treat success if response is empty array or not an error
        return true;
    }
}
