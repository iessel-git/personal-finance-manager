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

    /** Insert REAL budget */
    public void addBudget(Budget budget) {
        try {
            JSONObject json = new JSONObject();
            json.put("category_id", budget.getCategoryId());
            json.put("limit_amount", budget.getLimitAmount());
            json.put("is_test", false); // <-- ensures production data is marked clean

            client.insert("budget", json.toString());
            System.out.println("✔ Budget added!");

        } catch (Exception e) {
            System.out.println("❌ Failed to add budget");
            e.printStackTrace();
        }
    }

    /** Insert TEMPORARY BUDGET for testing (auto-deletes later) */
    public void addTestBudget(Budget budget) {
        try {
            JSONObject json = new JSONObject();
            json.put("category_id", budget.getCategoryId());
            json.put("limit_amount", budget.getLimitAmount());
            json.put("is_test", true); // <-- mark temporary

            client.insert("budget", json.toString());
            System.out.println("🧪 Test budget added!");

        } catch (Exception e) {
            System.out.println("❌ Failed to add test budget");
            e.printStackTrace();
        }
    }

    /** Internal: Delete all temporary test rows */
    private void deleteTestBudgets() {
        try {
            client.delete("budget", "is_test=eq.true");
            // Optional quiet log
            // System.out.println("🧹 Auto-clean test budgets");
        } catch (Exception e) {
            System.out.println("❌ Failed to auto-clean test budgets");
            e.printStackTrace();
        }
    }

    /** Fetch all REAL budgets */
    public List<Budget> getBudgets() {

        // AUTO CLEAN before returning data
        deleteTestBudgets();

        List<Budget> list = new ArrayList<>();

        try {
            String response = client.select("budget");
            JSONArray arr = new JSONArray(response);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                // Skip test data if any still exist
                if (o.optBoolean("is_test", false)) continue;

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

    /** Fetch a single REAL budget by categoryId */
    public Budget getBudgetByCategoryId(int categoryId) {

        // auto clean test data always
        deleteTestBudgets();

        try {
            String response = client.select("budget", "category_id=eq." + categoryId);
            JSONArray arr = new JSONArray(response);

            if (arr.length() == 0) return null;

            JSONObject o = arr.getJSONObject(0);

            // skip temp data
            if (o.optBoolean("is_test", false)) return null;

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

    /** Calculate total spent for a category */
    public double getTotalSpent(int categoryId) {
        try {
            String response = client.select("expense", "category_id=eq." + categoryId);
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

    /** Check if budget is exceeded */
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
