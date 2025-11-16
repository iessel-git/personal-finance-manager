package com.cse.financeapp;

import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

public class MainFinanceTest {

    public static void main(String[] args) throws Exception {
        System.out.println("\n========== PERSONAL FINANCE SYSTEM TEST ==========\n");

        SupabaseClient client = new SupabaseClient();

        runCategoryCrudTest(client);
        runExpenseCrudTest(client);

        System.out.println("========== ALL TESTS COMPLETE ==========\n");
    }

    // ================================================================
    // Helper: Safely convert response to JSONArray
    // ================================================================
    private static JSONArray safeArray(String response) {
        try {
            return new JSONArray(response);
        } catch (Exception ex) {
            System.out.println("⚠ Warning: Response was not a JSON array. Returning empty array.");
            System.out.println("Response: " + response);
            return new JSONArray();
        }
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

        JSONArray createdArr = safeArray(client.insert("categories", createJson.toString()));

        if (createdArr.isEmpty()) {
            throw new RuntimeException("❌ Category creation failed!");
        }

        int categoryId = createdArr.getJSONObject(0).getInt("id");
        System.out.println("✔ Created category ID: " + categoryId);

        // ----------------------------------------------------
        // 2. READ CATEGORIES
        // ----------------------------------------------------
        System.out.println("→ Fetching categories...");
        JSONArray allCats = safeArray(client.select("categories"));

        System.out.println("✔ Total categories found: " + allCats.length());

        // ----------------------------------------------------
        // 3. UPDATE CATEGORY
        // ----------------------------------------------------
        System.out.println("→ Updating category description...");

        JSONObject updateJson = new JSONObject();
        updateJson.put("id", categoryId);
        updateJson.put("description", "Updated Desc");

        JSONArray updatedArr = safeArray(client.upsert("categories", updateJson.toString()));

        if (updatedArr.isEmpty()) {
            throw new RuntimeException("❌ Category update failed!");
        }

        System.out.println("✔ Category updated!");

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
        createJson.put("category_id", 1); // must exist

        JSONArray createdArr = safeArray(client.insert("expenses", createJson.toString()));

        if (createdArr.isEmpty()) {
            throw new RuntimeException("❌ Expense creation failed!");
        }

        int expenseId = createdArr.getJSONObject(0).getInt("id");
        System.out.println("✔ Created expense ID: " + expenseId);

        // ----------------------------------------------------
        // 2. READ EXPENSES
        // ----------------------------------------------------
        System.out.println("→ Fetching expenses...");
        JSONArray allExp = safeArray(client.select("expenses"));

        System.out.println("✔ Total expenses found: " + allExp.length());

        // ----------------------------------------------------
        // 3. UPDATE EXPENSE
        // ----------------------------------------------------
        System.out.println("→ Updating expense amount...");

        JSONObject updateJson = new JSONObject();
        updateJson.put("id", expenseId);
        updateJson.put("amount", 40.75);

        JSONArray updatedArr = safeArray(client.upsert("expenses", updateJson.toString()));

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
