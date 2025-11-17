package com.cse.financeapp;

import com.cse.financeapp.dao.CategoryService;
import com.cse.financeapp.dao.ExpenseService;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Integration-style test runner that:
 *  - creates category
 *  - creates expense tied to that category
 *  - updates both
 *  - deletes both
 *
 * Uses Version B SupabaseClient (raw responses; services throw on errors).
 */
public class MainFinanceTest {

    public static void main(String[] args) {
        System.out.println("\n========== PERSONAL FINANCE SYSTEM TEST ==========\n");

        try {
            SupabaseClient client = new SupabaseClient();
            CategoryService categoryService = new CategoryService(client);
            ExpenseService expenseService = new ExpenseService(client);

            // ---------- Category CRUD ----------
            System.out.println("=== CATEGORY CRUD ===");
            // Clean old test categories (non-throwing)
            try { client.deleteWhere("categories", "name", "JUnitCategory"); } catch (Exception ignore) {}

            int catId = categoryService.createCategory("JUnitCategory", "CRUD Test");
            System.out.println("Created category id=" + catId);

            List<JSONObject> cats = categoryService.listRawCategories();
            System.out.println("Total categories: " + cats.size());

            boolean updatedCat = categoryService.updateCategory(catId, "Updated Name", "Updated Desc");

            System.out.println("Category update result=" + updatedCat);

            // ---------- Expense CRUD ----------
            System.out.println("\n=== EXPENSE CRUD ===");
            int expId = expenseService.createExpense("JUnitExpense", 25.50, LocalDate.now(), catId);
            System.out.println("Created expense id=" + expId);

            List<JSONObject> exps = expenseService.listRawExpenses();
            System.out.println("Total expenses: " + exps.size());

            boolean updatedExp = expenseService.updateExpenseAmount(expId, 99.99);
            System.out.println("Expense update result=" + updatedExp);

            // ---------- Cleanup ----------
            System.out.println("\n=== CLEANUP ===");
            boolean delExp = expenseService.deleteExpense(expId);
            System.out.println("Deleted expense result=" + delExp);

            boolean delCat = categoryService.deleteCategory(catId);
            System.out.println("Deleted category result=" + delCat);

            System.out.println("\n========== ALL TESTS COMPLETE ==========\n");

        } catch (Exception e) {
            System.err.println("TEST RUN FAILED: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
