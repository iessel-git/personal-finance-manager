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

        System.out.println("=== Adding Test Category ===");
        DatabaseHelper.addCategory("Test Category", "Just a test");

        System.out.println("=== Adding Test Expense ===");
        DatabaseHelper.addExpense("Groceries", 150.75, 1);

        System.out.println("\n========== PERSONAL FINANCE SYSTEM TEST ==========\n");

        // Initialize client
        SupabaseClient client = new SupabaseClient();

        CategoryRepository categoryRepo = new CategoryRepository(client);
        ExpenseService expenseService = new ExpenseService(client);

        // -----------------------------------------------------
        // CATEGORY TEST
        // -----------------------------------------------------
        System.out.println("=== Adding Test Category ===");
        Category testCategory = new Category(0, "CombinedTestCat", "For combined test");
        categoryRepo.addCategory(testCategory);
        System.out.println("✔ Category added!");

        System.out.println("\n=== Fetching Categories ===");
        List<Category> categories = categoryRepo.getAllCategories();
        for (Category c : categories) {
            System.out.println(c.getId() + " | " + c.getName() + " | " + c.getDescription());
        }

        int lastCategoryId = categories.get(categories.size() - 1).getId();

        // -----------------------------------------------------
        // EXPENSE TEST
        // -----------------------------------------------------
        System.out.println("\n=== Adding Test Expense ===");
        Expense testExpense = new Expense(
                0,
                "Test Expense Linked",
                20.75,
                LocalDate.now(),
                lastCategoryId  // use the category we just created
        );

        expenseService.addExpense(testExpense);
        System.out.println("✔ Expense added!");

        System.out.println("\n=== Fetching Expenses ===");
        List<Expense> expenses = expenseService.getExpenses();
        for (Expense e : expenses) {
            System.out.println(
                e.getId() + " | " + e.getDescription() + " | " +
                e.getAmount() + " | " + e.getDate() +
                " | Category: " + e.getCategoryId()
            );
        }

        int lastExpenseId = expenses.get(expenses.size() - 1).getId();

        // -----------------------------------------------------
        // DELETE TEST
        // -----------------------------------------------------
        System.out.println("\n=== Deleting Created Records ===");

        System.out.println("Deleting Category ID: " + lastCategoryId);
        categoryRepo.deleteCategory(lastCategoryId);
        System.out.println("✔ Category deleted!");

        System.out.println("Deleting Expense ID: " + lastExpenseId);
        expenseService.deleteExpense(lastExpenseId);
        System.out.println("✔ Expense deleted!");

        System.out.println("\n========== TEST COMPLETE ==========\n");
    }
}
