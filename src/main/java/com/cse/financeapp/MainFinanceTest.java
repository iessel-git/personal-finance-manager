package com.cse.financeapp;

import com.cse.financeapp.dao.CategoryService;
import com.cse.financeapp.dao.ExpenseService;
import com.cse.financeapp.models.Expense;
import com.cse.financeapp.service.SupabaseClient;

import java.time.LocalDate;
import java.util.List;

public class MainFinanceTest {

    public static void main(String[] args) {
        System.out.println("\n========== PERSONAL FINANCE SYSTEM TEST ==========\n");

        try {
            SupabaseClient client = new SupabaseClient();
            CategoryService categoryService = new CategoryService(client);
            ExpenseService expenseService = new ExpenseService(client);

            // CATEGORY CRUD
            System.out.println("=== CATEGORY CRUD ===");
            // attempt to clean any existing test category (by name)
            try {
                client.deleteWhere("categories", "name", "JUnitCategory");
            } catch (Exception ignored) {}

            int catId = categoryService.createCategory("JUnitCategory", "Created by CI test");
            System.out.println("Created category id=" + catId);

            List<?> cats = categoryService.listCategories();
            System.out.println("Total categories: " + cats.size());

            boolean updated = categoryService.updateCategory(catId, null, "Updated Desc");
            System.out.println("Update result: " + updated);

            // EXPENSE CRUD
            System.out.println("\n=== EXPENSE CRUD ===");
            Expense e = new Expense("JUnitExpense", 12.34, LocalDate.now(), catId);
            int expenseId = expenseService.addExpense(e);
            System.out.println("Created expense id=" + expenseId);

            List<Expense> expenses = expenseService.getExpenses();
            System.out.println("Total expenses: " + expenses.size());

            boolean expenseUpdated = expenseService.updateExpense(expenseId, "JUnitExpense Updated", 20.00, null, null, null);
            System.out.println("Expense update: " + expenseUpdated);

            boolean expenseDeleted = expenseService.deleteExpense(expenseId);
            System.out.println("Expense deleted: " + expenseDeleted);

            boolean catDeleted = categoryService.deleteCategory(catId);
            System.out.println("Category deleted: " + catDeleted);

            System.out.println("\n========== TEST COMPLETE ==========\n");

        } catch (Exception ex) {
            System.out.println("TEST RUN FAILED: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
