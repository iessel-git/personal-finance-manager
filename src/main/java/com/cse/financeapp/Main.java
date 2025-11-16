package com.example.financeapp;

import com.example.financeapp.dao.ExpenseService;
import com.example.financeapp.models.Expense;
import com.example.financeapp.service.SupabaseClient;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 🔥 Replace with your actual Supabase details
        String SUPABASE_URL = "https://ggjvorvnrrbqixszpemk.supabase.co";
        String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdnanZvcnZucnJicWl4c3pwZW1rIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMyNDAzNzcsImV4cCI6MjA3ODgxNjM3N30.jPcgtyajOl0qM35XCPWAygmWeH2ecHMTMxBOCF5IVtU";

        // Initialize client + services
        SupabaseClient client = new SupabaseClient(SUPABASE_URL, API_KEY);
        ExpenseService expenseService = new ExpenseService(client);

        System.out.println("\n=== Adding Test Expense ===");

        // Create a test expense
        Expense testExpense = new Expense(
                0,
                "Test Lunch",
                12.50,
                LocalDate.now(),
                1 // category_id
        );

        // Add the expense
        expenseService.addExpense(testExpense);

        System.out.println("\n=== Fetching All Expenses ===");
        List<Expense> expenses = expenseService.getExpenses();

        for (Expense e : expenses) {
            System.out.println(
                    e.getId() + " | " + e.getDescription() + " | " +
                    e.getAmount() + " | " + e.getDate() + " | Category: " + e.getCategoryId()
            );
        }

        // Optional: Delete the last inserted
        if (!expenses.isEmpty()) {
            int deleteId = expenses.get(expenses.size() - 1).getId();
            System.out.println("\n=== Deleting Expense with ID " + deleteId + " ===");
            expenseService.deleteExpense(deleteId);
        }
    }
}
