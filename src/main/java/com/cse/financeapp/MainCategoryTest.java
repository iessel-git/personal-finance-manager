package com.cse.financeapp;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.repository.CategoryRepository;
import com.cse.financeapp.service.SupabaseClient;

import java.util.List;

public class MainCategoryTest {
    public static void main(String[] args) {
        // Initialize Supabase client
        SupabaseClient client = new SupabaseClient();
        CategoryRepository categoryRepo = new CategoryRepository(client);

        System.out.println("\n=== Adding Test Category ===");

        // Create test category
        Category testCategory = new Category(
                0,
                "Test Category",
                "This is a test category"
        );

        categoryRepo.addCategory(testCategory);
        System.out.println("✔ Category added!");

        System.out.println("\n=== Fetching All Categories ===");
        List<Category> categories = categoryRepo.getCategories();
        for (Category c : categories) {
            System.out.println(c.getId() + " | " + c.getName() + " | " + c.getDescription());
        }

        // Optional: delete last added category
        if (!categories.isEmpty()) {
            int deleteId = categories.get(categories.size() - 1).getId();
            System.out.println("\n=== Deleting Category with ID " + deleteId + " ===");
            categoryRepo.deleteCategory(deleteId);
            System.out.println("✔ Category deleted!");
        }
    }
}
