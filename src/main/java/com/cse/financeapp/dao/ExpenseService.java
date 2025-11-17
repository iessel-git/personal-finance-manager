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

    public int createExpense(String description, double amount, LocalDate date, int categoryId) throws Exception {
        JSONObject j = new JSONObject();
        j.put("description", description);
        j.put("amount", amount);
        j.put("date", date.toString());
        j.put("category_id", categoryId);
        String resp = client.insert("expenses", j.toString());
        JSONArray arr = new JSONArray(resp);
        if (arr.isEmpty()) throw new RuntimeException("Expense insert returned empty");
        return arr.getJSONObject(0).getInt("id");
    }

    public List<Expense> listExpenses() throws Exception {
        String resp = client.select("expenses");
        JSONArray arr = new JSONArray(resp);
        List<Expense> out = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            out.add(new Expense(
                    o.optInt("id", -1),
                    o.optString("description", null),
                    o.optDouble("amount", 0.0),
                    LocalDate.parse(o.optString("date", LocalDate.now().toString())),
                    o.optInt("category_id", -1)
            ));
        }
        return out;
    }

    public boolean updateExpense(int id, String description, Double amount, LocalDate date, Integer categoryId) throws Exception {
        JSONObject j = new JSONObject();
        if (description != null) j.put("description", description);
        if (amount != null) j.put("amount", amount);
        if (date != null) j.put("date", date.toString());
        if (categoryId != null) j.put("category_id", categoryId);
        String resp = client.update("expenses", id, j.toString());
        JSONArray arr = new JSONArray(resp);
        return !arr.isEmpty();
    }

    public boolean deleteExpense(int id) throws Exception {
        client.delete("expenses", id);
        return true;
    }
}
