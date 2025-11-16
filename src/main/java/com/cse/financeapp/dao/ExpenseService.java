package com.cse.financeapp.dao;

import com.cse.financeapp.models.Expense;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseService {

    private final SupabaseClient client;

    public ExpenseService(SupabaseClient client) {
        this.client = client;
    }

    /**
     * Add a new expense to Supabase
     */
    public void addExpense(Expense expense) {
        try {
            JSONObject json = new JSONObject();
            json.put("description", expense.getDescription());
            json.put("amount", expense.getAmount());
            json.put("date", expense.getDate().toString());
            json.put("category_id", expense.getCategoryId());

            client.post("/rest/v1/expenses", json.toString());
            System.out.println("✔ Expense added to Supabase!");

        } catch (Exception e) {
            System.out.println("❌ Failed to add expense");
            e.printStackTrace();
        }
    }

    /**
     * Get all expenses from Supabase
     */
    public List<Expense> getExpenses() {
        List<Expense> list = new ArrayList<>();

        try {
            String response = client.get("/rest/v1/expenses?select=*");

            JSONArray arr = new JSONArray(response);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                list.add(new Expense(
                        o.getInt("id"),
                        o.getString("description"),
                        o.getDouble("amount"),
                        LocalDate.parse(o.getString("date")),
                        o.getInt("category_id")
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to fetch expenses from Supabase");
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Delete an expense by ID
     */
    public void deleteExpense(int id) {
        try {
            client.delete("/rest/v1/expenses?id=eq." + id);
            System.out.println("✔ Expense deleted from Supabase!");

        } catch (Exception e) {
            System.out.println("❌ Failed to delete expense");
            e.printStackTrace();
        }
    }
}
