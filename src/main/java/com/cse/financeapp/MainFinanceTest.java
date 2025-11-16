package com.cse.financeapp;

import com.cse.financeapp.models.Category;
import com.cse.financeapp.models.Expense;
import com.cse.financeapp.repository.CategoryRepository;
import com.cse.financeapp.dao.ExpenseService;
import com.cse.financeapp.service.SupabaseClient;

import java.time.LocalDate;
import java.util.List;

public class MainFinanceTest {

    public static void main(String[] args) throws Exception {

        System.out.println("\n========== PERSONAL FINANCE SYSTEM TEST ==========\n");

        // Initialize client and repos
        SupabaseClient client = new SupabaseClient();
        CategoryRepository categoryRepo = new CategoryRepository(client);
        ExpenseService expenseService = new ExpenseService(client);

        // -----------------------------------------------------
        // 1️⃣ CATEGORY TEST
        // -----------------------------------------------------
        System.out.println("=== CATEGORY TEST ===");

        Category testCategory = new Category(
                0,
                "CombinedTestCat",
                "For combined integration test"
        );

        // Add category
        categoryRepo.addCategory(testCategory);
        System.out.println("✔ Category added!");

        // Fetch categories
        System.out.println("\n=== Fetching Categories ===");
        List<Category> categories = categoryRepo.getAllCategories();

        printCategories(categories);

        // Track last category
        int newCategoryId = categories.get(categories.size() - 1).getId();


        // -----------------------------------------------------
        // 2️⃣ EXPENSE TEST
        // -----------------------------------------------------
        System.out.println("\n=== EXPENSE TEST ===");

        Expense expense1 = new Expense(
                0,
                "Test Expense Linked",
                20.75,
                LocalDate.now(),
                newCategoryId
        );

        expenseService.addExpense(expense1);
        System.out.println("✔ Expense added!");

        // Fetch expenses
        System.out.println("\n=== Fetching Expenses ===");
        List<Expense> expenses = expenseService.getExpenses();

        printExpenses(expenses);

        // Track last expense
        int lastExpenseId = expenses.get(expenses.size() - 1).getId();


        // -----------------------------------------------------
        // 3️⃣ CLEAN UP (Delete created test data)
        // -----------------------------------------------------
        System.out.println("\n=== CLEANUP: Deleting Test Records ===");

        System.out.println("Deleting Expense ID: " + lastExpenseId);
        expenseService.deleteExpense(lastExpenseId);
        System.out.println("✔ Expense deleted!");

        System.out.println("Deleting Category ID: " + newCategoryId);
        categoryRepo.deleteCategory(newCategoryId);
        System.out.println("✔ Category deleted!");

        System.out.println("\n========== TEST COMPLETE ==========\n");
    }


    // -----------------------------------------------------
    // Helper: Print categories neatly
    // -----------------------------------------------------
    private static void printCategories(List<Category> categories) {
        System.out.println("ID | Name | Description");
        System.out.println("----------------------------------");
        for (Category c : categories) {
            System.out.println(c.getId() + " | " + c.getName() + " | " + c.getDescription());
        }
    }

    // -----------------------------------------------------
    // Helper: Print expenses neatly
    // -----------------------------------------------------
    private static void printExpenses(List<Expense> expenses) {
        System.out.println("ID | Description | Amount | Date | Category");
        System.out.println("-------------------------------------------------------------");

        for (Expense e : expenses) {
            System.out.println(
                e.getId() + " | " +
                e.getDescription() + " | " +
                e.getAmount() + " | " +
                e.getDate() + " | Category: " +
                e.getCategoryId()
            );
        }
    }
}
