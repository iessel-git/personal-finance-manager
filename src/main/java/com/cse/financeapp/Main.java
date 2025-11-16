package com.example.financeapp;

import com.example.financeapp.dao.ExpenseService;
import com.example.financeapp.models.Expense;
import com.example.financeapp.service.SupabaseClient;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        SupabaseClient client = new SupabaseClient(
                "https://finance_manager.supabase.co",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdnanZvcnZucnJicWl4c3pwZW1rIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMyNDAzNzcsImV4cCI6MjA3ODgxNjM3N30.jPcgtyajOl0qM35XCPWAygmWeH2ecHMTMxBOCF5IVtU"
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
