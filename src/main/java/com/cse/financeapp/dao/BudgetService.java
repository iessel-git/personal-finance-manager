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

    // ------------------------------------------------------
    // CREATE
    // ------------------------------------------------------
    public void addBudget(Budget budget) {
        try {
            JSONObject json = new JSONObject();
            json.put("category_id", budget.getCategoryId());
            json.put("limit_amount", budget.getLimitAmount());

            client.insert("budget", json.toString());
            System.out.println("✔ Budget added");

        } catch (Exception e) {
            System.out.println("❌ Failed to add budget");
            e.printStackTrace();
        }
    }


    // ------------------------------------------------------
    // READ ALL
    // ------------------------------------------------------
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


    // ------------------------------------------------------
    // READ SINGLE (by category)
    // ------------------------------------------------------
    public Budget getBudgetByCategoryId(int categoryId) {
        try {
            String response = client.select("budget?category_id=eq." + categoryId);
            JSONArray arr = new JSONArray(response);

            if (arr.length() == 0) return null;

            JSONObject o = arr.getJSONObject(0);

            return new Budget(
                    o.getInt("id"),
                    o.getInt("category_id"),
                    o.getDouble("limit_amount")
            );

        } catch (Exception e) {
            System.out.println("❌ Failed to get budget by category");
            e.printStackTrace();
            return null;
        }
    }


    // ------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------
    public void updateBudget(int id, double newLimit) {
        try {
            JSONObject json = new JSONObject();
            json.put("limit_amount", newLimit);

            client.update("budget?id=eq." + id, json.toString());

            System.out.println("✔ Budget updated");

        } catch (Exception e) {
            System.out.println("❌ Failed to update budget");
            e.printStackTrace();
        }
    }


    // ------------------------------------------------------
    // DELETE
    // ------------------------------------------------------
    public void deleteBudget(int id) {
        try {
            client.delete("budget", "id=eq." + id);
            System.out.println("✔ Budget deleted");

        } catch (Exception e) {
            System.out.println("❌ Failed to delete budget");
            e.printStackTrace();
        }
    }


    // ------------------------------------------------------
    // TOTAL SPENDING FOR CATEGORY
    // ------------------------------------------------------
    public double getTotalSpent(int categoryId) {
        try {
            String response = client.select("expense?category_id=eq." + categoryId);
            JSONArray arr = new JSONArray(response);

            double sum = 0;
            for (int i = 0; i < arr.length(); i++) {
                sum += arr.getJSONObject(i).getDouble("amount");
            }

            return sum;

        } catch (Exception e) {
            System.out.println("❌ Failed calculating total spent");
            e.printStackTrace();
            return 0;
        }
    }


    // ------------------------------------------------------
    // BUDGET REMAINING
    // ------------------------------------------------------
    public double getRemainingAmount(int categoryId) {
        Budget budget = getBudgetByCategoryId(categoryId);
        if (budget == null) return 0;

        return budget.getLimitAmount() - getTotalSpent(categoryId);
    }


    // ------------------------------------------------------
    // CHECK IF EXCEEDED
    // ------------------------------------------------------
    public boolean isBudgetExceeded(int categoryId) {
        Budget budget = getBudgetByCategoryId(categoryId);
        if (budget == null) return false;

        return getTotalSpent(categoryId) > budget.getLimitAmount();
    }
}
