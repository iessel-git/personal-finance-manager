package com.cse.financeapp;

import com.cse.financeapp.dao.CategoryService;
import com.cse.financeapp.dao.ExpenseService;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            SupabaseClient client = new SupabaseClient();
            CategoryService categoryService = new CategoryService(client);
            ExpenseService expenseService = new ExpenseService(client);

            System.out.println("=== DEMO ===");

            // CREATE CATEGORY
            int catId = categoryService.createCategory("DemoCategory", "For Main.java run");
            System.out.println("Created category id=" + catId);

            // CREATE EXPENSE
            int expId = expenseService.createExpense(
                    "DemoExpense",
                    40.25,
                    LocalDate.now(),
                    catId
            );
            System.out.println("Created expense id=" + expId);

            // LIST ALL EXPENSES
            List<JSONObject> expenses = expenseService.listRawExpenses();
            System.out.println("Total expenses: " + expenses.size());

            // CLEAN-UP
            expenseService.deleteExpense(expId);
            categoryService.deleteCategory(catId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
