package com.cse.financeapp.dao;

import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ExpenseService — uses SupabaseClient (Version B).
 */
public class ExpenseService {

    private final SupabaseClient client;

    public ExpenseService(SupabaseClient client) {
        this.client = client;
    }

    public int createExpense(String description, double amount, LocalDate date, int categoryId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("description", description);
        json.put("amount", amount);
        json.put("date", date.toString());
        json.put("category_id", categoryId);

        String resp = client.insert("expenses", json.toString());
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            if (arr.isEmpty()) throw new RuntimeException("Insert returned empty array");
            return arr.getJSONObject(0).getInt("id");
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on createExpense: " + o.toString());
        }
    }

    public List<JSONObject> listRawExpenses() throws Exception {
        String resp = client.select("expenses");
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            List<JSONObject> out = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) out.add(arr.getJSONObject(i));
            return out;
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on select expenses: " + o.toString());
        }
    }

    public boolean updateExpenseAmount(int id, double newAmount) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("amount", newAmount);

        String resp = client.upsert("expenses", json.toString());
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on updateExpense: " + o.toString());
        }
    }

    public boolean deleteExpense(int id) throws Exception {
        String resp = client.delete("expenses", id);
        if (isArray(resp)) {
            JSONArray arr = new JSONArray(resp);
            return !arr.isEmpty();
        } else {
            JSONObject o = new JSONObject(resp);
            throw new RuntimeException("Supabase error on deleteExpense: " + o.toString());
        }
    }

    private boolean isArray(String raw) {
        if (raw == null) return false;
        String t = raw.trim();
        return t.startsWith("[");
    }
}
