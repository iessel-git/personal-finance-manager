package com.cse.financeapp;

import com.cse.financeapp.dao.ExpenseService;
import com.cse.financeapp.models.Expense;
import com.cse.financeapp.service.SupabaseClient;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // No need to pass URL or KEY now
        SupabaseClient client = new SupabaseClient();
        ExpenseService expenseService = new ExpenseService(client);

        System.out.println("\n=== Adding Test Expense ===");

        Expense testExpense = new Expense(
            0,
            "Test Lunch",
            12.50,
            LocalDate.now(),
            1
        );

        expenseService.addExpense(testExpense);

        System.out.println("\n=== Fetching All Expenses ===");
        List<Expense> expenses = expenseService.getExpenses();

        for (Expense e : expenses) {
            System.out.println(
                e.getId() + " | " + e.getDescription() + " | " +
                e.getAmount() + " | " + e.getDate() + " | Category: " + e.getCategoryId()
            );
        }

        if (!expenses.isEmpty()) {
            int deleteId = expenses.get(expenses.size() - 1).getId();
            System.out.println("\n=== Deleting Expense with ID " + deleteId + " ===");
            expenseService.deleteExpense(deleteId);
        }
    }
}
