package com.example.financeapp.dao;

import com.example.financeapp.models.Category;
import com.example.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private final SupabaseClient client;

    public CategoryService(SupabaseClient client) {
        this.client = client;
    }

    public void addCategory(Category category) {
        try {
            JSONObject json = new JSONObject();
            json.put("name", category.getName());

            client.post("/rest/v1/categories", json.toString());
            System.out.println("✔ Category added!");

        } catch (Exception e) {
            System.out.println("❌ Failed to add category");
            e.printStackTrace();
        }
    }

    public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();

        try {
            String response = client.get("/rest/v1/categories?select=*");
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
}

