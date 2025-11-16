package com.cse.financeapp;

import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

public class MainFinanceTest {

    public static void main(String[] args) throws Exception {
        System.out.println("\n========== PERSONAL FINANCE SYSTEM TEST ==========");

        SupabaseClient client = new SupabaseClient();

        runCategoryCrudTest(client);
        runExpenseCrudTest(client);

        System.out.println("========== ALL TESTS COMPLETE ==========\n");
    }

    // ======================================================================
    // CATEGORY CRUD TEST
    // ======================================================================
    public static void runCategoryCrudTest(SupabaseClient client) throws Exception {
        System.out.println("\n========== CATEGORY CRUD TEST ==========");

        // ----------------------------------------------------
        // 1. CREATE CATEGORY
        // ----------------------------------------------------
        System.out.println("→ Creating test category...");

        JSONObject createJson = new JSONObject();
        createJson.put("name", "JUnitCategory");
        createJson.put("description", "CRUD Test");

        String createResp = client.insert("categories", createJson.toString());
        JSONArray createdArr = new JSONArray(createResp);

        if (createdArr.isEmpty()) {
            throw new RuntimeException("❌ Category creation failed!");
        }

        int categoryId = createdArr.getJSONObject(0).getInt("id");
        System.out.println("✔ Created category ID: " + categoryId);

        // ----------------------------------------------------
        // 2. READ CATEGORIES
        // ----------------------------------------------------
        System.out.println("→ Fetching categories...");
        String fetchResp = client.select("categories");
        JSONArray allCats = new JSONArray(fetchResp);

        System.out.println("✔ Total categories: " + allCats.length());

        // ----------------------------------------------------
        // 3. UPDATE CATEGORY
        // ----------------------------------------------------
        System.out.println("→ Updating category description...");
        JSONObject updateJson = new JSONObject();
        updateJson.put("id", categoryId);
        updateJson.put("description", "Updated Desc");

        String updateResp = client.upsert("categories", updateJson.toString());
        JSONArray updatedArr = new JSONArray(updateResp);

        if (updatedArr.isEmpty()) {
            throw new RuntimeException("❌ Category update failed!");
        }

        System.out.println("✔ Updated category!");

        // ----------------------------------------------------
        // 4. DELETE CATEGORY
        // ----------------------------------------------------
        System.out.println("→ Deleting test category ID: " + categoryId);
        client.delete("categories", categoryId);
        System.out.println("✔ Category deleted!");

        System.out.println("========== CATEGORY TEST COMPLETE ==========\n");
    }

    // ======================================================================
    // EXPENSE CRUD TEST
    // ======================================================================
    public static void runExpenseCrudTest(SupabaseClient client) throws Exception {
        System.out.println("\n========== EXPENSE CRUD TEST ==========");

        // ----------------------------------------------------
        // 1. CREATE EXPENSE
        // ----------------------------------------------------
        System.out.println("→ Creating test expense...");

        JSONObject createJson = new JSONObject();
        createJson.put("description", "JUnitExpense");
        createJson.put("amount", 25.50);
        createJson.put("date", LocalDate.now().toString());
        createJson.put("category_id", 1); // A valid category must exist

        String createResp = client.insert("expenses", createJson.toString());
        JSONArray createdArr = new JSONArray(createResp);

        if (createdArr.isEmpty()) {
            throw new RuntimeException("❌ Expense creation failed!");
        }

        int expenseId = createdArr.getJSONObject(0).getInt("id");
        System.out.println("✔ Created expense ID: " + expenseId);

        // ----------------------------------------------------
        // 2. READ EXPENSES
        // ----------------------------------------------------
        System.out.println("→ Fetching expenses...");
        String fetchResp = client.select("expenses");
        JSONArray allExp = new JSONArray(fetchResp);

        System.out.println("✔ Total expenses: " + allExp.length());

        // ----------------------------------------------------
        // 3. UPDATE EXPENSE
        // ----------------------------------------------------
        System.out.println("→ Updating expense amount...");

        JSONObject updateJson = new JSONObject();
        updateJson.put("id", expenseId);
        updateJson.put("amount", 40.75);

        String updateResp = client.upsert("expenses", updateJson.toString());
        JSONArray updatedArr = new JSONArray(updateResp);

        if (updatedArr.isEmpty()) {
            throw new RuntimeException("❌ Expense update failed!");
        }

        System.out.println("✔ Expense updated!");

        // ----------------------------------------------------
        // 4. DELETE EXPENSE
        // ----------------------------------------------------
        System.out.println("→ Deleting test expense ID: " + expenseId);
        client.delete("expenses", expenseId);
        System.out.println("✔ Expense deleted!");

        System.out.println("========== EXPENSE TEST COMPLETE ==========\n");
    }
}
