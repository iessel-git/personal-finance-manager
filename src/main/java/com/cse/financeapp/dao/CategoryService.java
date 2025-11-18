package com.cse.financeapp.dao;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.repository.CategoryRepository;
import com.cse.financeapp.service.SupabaseClient;

import java.util.List;

public class CategoryService {
    private final CategoryRepository repo;

    public CategoryService(SupabaseClient client) {
        this.repo = new CategoryRepository(client);
    }

    public void addCategory(Category category) {
        try {
            repo.addCategory(category);
            System.out.println("✔ Category added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Category> getCategories() {
        try {
            return repo.getAllCategories();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteCategory(int id) {
        try {
            repo.deleteCategory(id);
            System.out.println("✔ Category deleted!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
