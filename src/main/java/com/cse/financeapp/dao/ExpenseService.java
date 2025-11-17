package com.cse.financeapp.dao;

import com.cse.financeapp.models.Expense;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ExpenseService - CRUD operations against Supabase expenses table
 */
public class ExpenseService {

    private final SupabaseClient client;

    public ExpenseService(SupabaseClient client) {
        this.client = client;
    }

    public int addExpense(Expense expense) throws Exception {
        JSONObject json = new JSONObject();
        json.put("description", expense.getDescription());
        json.put("amount", expense.getAmount());
        json.put("date", expense.getDate().toString());
        json.put("category_id", expense.getCategoryId());
        if (expense.getNote() != null) json.put("note", expense.getNote());

        String resp = client.insert("expenses", json.toString());
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            if (arr.isEmpty()) throw new RuntimeException("Insert returned empty array");
            return arr.getJSONObject(0).getInt("id");
        } else {
            throw new RuntimeException("Supabase error on addExpense: " + resp);
        }
    }

    public List<Expense> getExpenses() throws Exception {
        String resp = client.select("expenses");
        List<Expense> out = new ArrayList<>();
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                out.add(new Expense(
                        o.getInt("id"),
                        o.optString("description", null),
                        o.optDouble("amount", 0.0),
                        LocalDate.parse(o.getString("date")),
                        o.has("category_id") ? o.optInt("category_id") : -1,
                        o.optString("note", null)
                ));
            }
            return out;
        } else {
            throw new RuntimeException("Supabase error on getExpenses: " + resp);
        }
    }

    // safe partial update (only non-null fields)
    public boolean updateExpense(int id, String description, Double amount, LocalDate date, Integer categoryId, String note) throws Exception {
        JSONObject json = new JSONObject();
        if (description != null) json.put("description", description);
        if (amount != null) json.put("amount", amount);
        if (date != null) json.put("date", date.toString());
        if (categoryId != null) json.put("category_id", categoryId);
        if (note != null) json.put("note", note);

        if (json.isEmpty()) throw new IllegalArgumentException("No fields to update for expense");

        String resp = client.patch("expenses", id, json.toString());
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            throw new RuntimeException("Supabase error on updateExpense: " + resp);
        }
    }

    public boolean deleteExpense(int id) throws Exception {
        String resp = client.delete("expenses", id);
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            throw new RuntimeException("Supabase error on deleteExpense: " + resp);
        }
    }

    private boolean isArray(String raw) {
        if (raw == null) return false;
        return raw.trim().startsWith("[");
    }
}
