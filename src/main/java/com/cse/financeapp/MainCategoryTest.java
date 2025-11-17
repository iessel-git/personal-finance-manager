package com.cse.financeapp;

import com.cse.financeapp.dao.CategoryService;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONObject;

import java.util.List;

public class MainCategoryTest {

    public static void main(String[] args) {
        try {
            SupabaseClient client = new SupabaseClient();
            CategoryService service = new CategoryService(client);

            System.out.println("=== CATEGORY TEST ===");

            // CREATE
            int id = service.createCategory("JUnitCategory", "Category Test");
            System.out.println("Created category id=" + id);

            // LIST
            List<JSONObject> list = service.listRawCategories();
            System.out.println("Total categories: " + list.size());

            // UPDATE
            boolean updated = service.updateCategory(id, "Updated Description");
            System.out.println("Updated successfully? " + updated);

            // DELETE
            boolean deleted = service.deleteCategory(id);
            System.out.println("Deleted successfully? " + deleted);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
