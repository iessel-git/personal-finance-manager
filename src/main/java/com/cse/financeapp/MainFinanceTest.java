package com.cse.financeapp;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.models.Expense;
import com.cse.financeapp.repository.CategoryRepository;
import com.cse.financeapp.dao.ExpenseService;
import com.cse.financeapp.service.SupabaseClient;

import java.util.List;
import java.time.LocalDate;

public class MainFinanceTest {

    public static void main(String[] args) throws Exception {

        System.out.println("\n========== PERSONAL FINANCE SYSTEM TEST ==========\n");

        // Initialize Supabase client (with your hardcoded credentials)
        SupabaseClient client = new SupabaseClient();
        CategoryRepository categoryRepo = new CategoryRepository(client);
        ExpenseService expenseService = new ExpenseService(client);

        // -----------------------------------------------------
        // 1️⃣ CATEGORY TEST
        // -----------------------------------------------------
        System.out.println("=== Adding Test Category ===");
        Category testCategory = new Category(
                0,
                "CombinedTestCat",
                "For combined integration test"
        );

        categoryRepo.addCategory(testCategory);
        System.out.println("✔ Category added!");

        // Fetch categories
        System.out.println("\n=== Fetching Categories ===");
        List<Category> categories = categoryRepo.getAllCategories();
        for (Category c : categories) {
            System.out.println(c.getId() + " | " + c.getName() + " | " + c.getDescription());
        }

        int insertedCategoryId = categories.get(categories.size() - 1).getId();


        // -----------------------------------------------------
        // 2️⃣ EXPENSE TEST
        // -----------------------------------------------------
        System.out.println("\n=== Adding Test Expense ===");
        Expense testExpense = new Expense(
                0,
                "Test Expense Linked",
                20.75,
                LocalDate.now(),
                insertedCategoryId   // link to created category
        );

        expenseService.addExpense(testExpense);
        System.out.println("✔ Expense added!");

        // Fetch expenses
        System.out.println("\n=== Fetching Expenses ===");
        List<Expense> expenses = expenseService.getExpenses();
        for (Expense e : expenses) {
            System.out.println(
                e.getId() + " | " + e.getDescription() + " | " +
                e.getAmount() + " | " + e.getDate() +
                " | Category: " + e.getCategoryId()
            );
        }

        int insertedExpenseId = expenses.get(expenses.size() - 1).getId();


        // -----------------------------------------------------
        // 3️⃣ CLEANUP TEST DATA
        // -----------------------------------------------------
        System.out.println("\n=== CLEANUP: Deleting Test Records ===");

        // Delete expense first → avoids FK constraint failures
        System.out.println("Deleting Expense ID: " + insertedExpenseId);
        expenseService.deleteExpense(insertedExpenseId);
        System.out.println("✔ Expense deleted!");

        System.out.println("Deleting Category ID: " + insertedCategoryId);
        categoryRepo.deleteCategory(insertedCategoryId);
        System.out.println("✔ Category deleted!");


        // -----------------------------------------------------
        System.out.println("\n========== TEST COMPLETE ==========\n");
    }
}
