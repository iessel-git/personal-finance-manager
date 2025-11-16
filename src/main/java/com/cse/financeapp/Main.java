package com.example.financeapp;

import com.example.financeapp.dao.ExpenseService;
import com.example.financeapp.models.Expense;
import com.example.financeapp.service.SupabaseClient;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        SupabaseClient client = new SupabaseClient(
                "https://YOUR_PROJECT.supabase.co",
                "YOUR_ANON_KEY"
        );

        ExpenseService expenseService = new ExpenseService(client);

        Expense expense = new Expense(
                "Lunch",
                12.50,
                LocalDate.now(),
                1
        );

        expenseService.addExpense(expense);

        System.out.println("All expenses:");
        expenseService.getExpenses().forEach(System.out::println);
    }
}
