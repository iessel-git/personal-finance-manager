package com.cse.financeapp.dao;

import com.cse.financeapp.models.Budget;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BudgetService {

    private final SupabaseClient client;

    public BudgetService(SupabaseClient client) {
        this.client = client;
    }

    /** Insert new budget */
    public void addBudget(Budget budget) {
        try {
            JSONObject json = new JSONObject();
            json.put("category_id", budget.getCategoryId());
            json.put("limit_amount", budget.getLimitAmount());

            client.insert("budget", json.toString());
            System.out.println("✔ Budget added!");

        } catch (Exception e) {
            System.out.println("❌ Failed to add budget");
            e.printStackTrace();
        }
    }

    /** Fetch all budgets */
    public List<Budget> getBudgets() {
        List<Budget> list = new ArrayList<>();

        try {
            String response = client.select("budget");
            JSONArray arr = new JSONArray(response);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                list.add(new Budget(
                        o.getInt("id"),
                        o.getInt("category_id"),
                        o.getDouble("limit_amount")
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to fetch budgets");
            e.printStackTrace();
        }

        return list;
    }

    /** Fetch budget by category */
    public Budget getBudgetByCategoryId(int categoryId) {
        try {
            String table = "budget?category_id=eq." + categoryId + "&select=*";
            String response = client.select(table);

            JSONArray arr = new JSONArray(response);
            if (arr.length() == 0) return null;

            JSONObject o = arr.getJSONObject(0);

            return new Budget(
                    o.getInt("id"),
                    o.getInt("category_id"),
                    o.getDouble("limit_amount")
            );

        } catch (Exception e) {
            System.out.println("❌ Failed to fetch budget by category");
            e.printStackTrace();
            return null;
        }
    }

    /** Total spent */
    public double getTotalSpent(int categoryId) {
        try {
            String table = "expense?category_id=eq." + categoryId + "&select=*";
            String response = client.select(table);

            JSONArray arr = new JSONArray(response);
            double sum = 0;

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                sum += o.getDouble("amount");
            }

            return sum;

        } catch (Exception e) {
            System.out.println("❌ Failed to calculate total spent");
            e.printStackTrace();
            return 0;
        }
    }

    /** Budget exceeded? */
    public boolean isBudgetExceeded(int categoryId) {
        Budget budget = getBudgetByCategoryId(categoryId);
        if (budget == null) return false;

        double spent = getTotalSpent(categoryId);
        return spent > budget.getLimitAmount();
    }

    /** Remaining amount */
    public double getRemainingAmount(int categoryId) {
        Budget budget = getBudgetByCategoryId(categoryId);
        if (budget == null) return 0;

        double spent = getTotalSpent(categoryId);
        return budget.getLimitAmount() - spent;
    }
}
