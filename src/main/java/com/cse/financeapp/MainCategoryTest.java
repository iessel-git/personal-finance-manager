package com.cse.financeapp;

import com.cse.financeapp.dao.CategoryService;
import com.cse.financeapp.models.Category;
import com.cse.financeapp.service.SupabaseClient;

import java.util.List;

public class MainCategoryTest {
    public static void main(String[] args) {

        // Use existing SupabaseClient with hardcoded key
        SupabaseClient client = new SupabaseClient();
        CategoryService categoryService = new CategoryService(client);

        System.out.println("\n=== Adding Test Category ===");
        Category testCategory = new Category(0, "Test Category", "Just a test");
        categoryService.addCategory(testCategory);

        System.out.println("\n=== Fetching All Categories ===");
        List<Category> categories = categoryService.getCategories();
        if (categories != null) {
            for (Category c : categories) {
                System.out.println(c.getId() + " | " + c.getName() + " | " + c.getDescription());
            }

            // Optional: delete last inserted category
            int deleteId = categories.get(categories.size() - 1).getId();
            System.out.println("\n=== Deleting Category ID " + deleteId + " ===");
            categoryService.deleteCategory(deleteId);
        }
    }
}
