package com.cse.financeapp.dao;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private final SupabaseClient client;

    public CategoryService(SupabaseClient client) {
        this.client = client;
    }

    // -------------------------------------------------------------
    // ADD CATEGORY
    // -------------------------------------------------------------
    public void addCategory(Category category) {
        try {
            JSONObject json = new JSONObject();
            json.put("name", category.getName());

            client.post("categories", json.toString());
            System.out.println("✔ Category added!");

        } catch (Exception e) {
            System.out.println("❌ Failed to add category");
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    // GET ALL CATEGORIES
    // -------------------------------------------------------------
    public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();

        try {
            String response = client.get("categories?select=*");
            JSONArray arr = new JSONArray(response);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                list.add(new Category(
                        o.getInt("id"),
                        o.getString("name")
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to fetch categories");
            e.printStackTrace();
        }

        return list;
    }

    // -------------------------------------------------------------
    // DELETE CATEGORY
    // -------------------------------------------------------------
    public void deleteCategory(int id) {
        try {
            client.delete("categories?id=eq." + id);
            System.out.println("✔ Category deleted!");

        } catch (Exception e) {
            System.out.println("❌ Failed to delete category");
            e.printStackTrace();
        }
    }
}
