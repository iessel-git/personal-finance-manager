package com.cse.financeapp;

import com.cse.financeapp.dao.CategoryService;
import com.cse.financeapp.dao.ExpenseService;
import com.cse.financeapp.models.Category;
import com.cse.financeapp.models.Expense;
import com.cse.financeapp.service.SupabaseClient;

import java.time.LocalDate;
import java.util.List;

/**
 * Minimal combined test for Category + Expense (Option A).
 * - Uses env vars SUPABASE_URL and SUPABASE_KEY
 */
public class MainFinanceTest {

    public static void main(String[] args) throws Exception {
        System.out.println("\n=== START: Combined Category+Expense Test ===");

        SupabaseClient client = new SupabaseClient();
        CategoryService categoryService = new CategoryService(client);
        ExpenseService expenseService = new ExpenseService(client);

        // 1) Create a unique category (timestamp) to avoid duplicate-name conflicts
        String uniqueName = "JUnitCategory_" + System.currentTimeMillis();
        int catId = categoryService.createCategory(uniqueName, "Created by CI test");
        System.out.println("Created category id=" + catId);

        // 2) Create an expense that references the category
        int expenseId = expenseService.createExpense("JUnitExpense", 9.99, LocalDate.now(), catId);
        System.out.println("Created expense id=" + expenseId);

        // 3) List categories and expenses (print counts)
        List<Category> cats = categoryService.listCategories();
        List<Expense> exps = expenseService.listExpenses();
        System.out.println("Total categories: " + cats.size());
        System.out.println("Total expenses: " + exps.size());

        // 4) Update the category description and expense amount
        boolean cUpdated = categoryService.updateCategory(catId, uniqueName, "Updated by CI test");
        boolean eUpdated = expenseService.updateExpense(expenseId, null, 19.95, null, null);
        System.out.println("Category updated: " + cUpdated + ", Expense updated: " + eUpdated);

        // 5) Cleanup
        boolean eDeleted = expenseService.deleteExpense(expenseId);
        boolean cDeleted = categoryService.deleteCategory(catId);
        System.out.println("Deleted expense: " + eDeleted + ", deleted category: " + cDeleted);

        System.out.println("=== FINISH: Combined test complete ===\n");
    }
}
