package com.cse.financeapp;

import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;

public class MainFinanceTest {

    private static final String TEST_CATEGORY_NAME = "JUnitCategory";
    private static final String TEST_CATEGORY_DESC = "JUnit Test Description";

    public static void main(String[] args) throws Exception {
        SupabaseClient client = new SupabaseClient();

        System.out.println("\n========== CATEGORY CRUD TEST ==========");
        runCategoryCrudTest(client);

        System.out.println("\n========== EXPENSE CRUD TEST ==========");
        runExpenseCrudTest(client);

        System.out.println("\n========== TEST COMPLETE ==========");
    }

    // -------------------------------------------------------------
    // CATEGORY CRUD TEST
    // -------------------------------------------------------------
    public static void runCategoryCrudTest(SupabaseClient client) throws Exception {

        System.out.println("→ Cleaning old test category...");
        client.deleteWhere("categories", "name", TEST_CATEGORY_NAME);

        // CREATE
        System.out.println("→ Creating test category...");
        String createResponse = client.insert("categories",
                "{ \"name\": \"" + TEST_CATEGORY_NAME + "\", \"description\": \"" + TEST_CATEGORY_DESC + "\" }"
        );

        // convert to array
        JSONArray createArr = new JSONArray(createResponse);
        if (createArr.length() == 0)
            throw new RuntimeException("❌ Category creation failed!");

        int categoryId = createArr.getJSONObject(0).getInt("id");
        System.out.println("✔ Category created: ID = " + categoryId);

        // READ
        String selectResponse = client.select("categories");
        JSONArray categories = new JSONArray(selectResponse);

        System.out.println("\n→ Category List:");
        for (int i = 0; i < categories.length(); i++) {
            System.out.println(categories.getJSONObject(i));
        }

        // DELETE
        System.out.println("\n→ Deleting test category...");
        client.delete("categories", categoryId);
        System.out.println("✔ Category deleted!");
    }

    // -------------------------------------------------------------
    // EXPENSE CRUD TEST
    // -------------------------------------------------------------
    public static void runExpenseCrudTest(SupabaseClient client) throws Exception {

        // PREP: Create category for expense
        System.out.println("→ Creating category for expense test...");
        String catRes = client.insert("categories", "{ \"name\": \"JUnitExpenseCat\" }");
        JSONArray catArr = new JSONArray(catRes);
        int categoryId = catArr.getJSONObject(0).getInt("id");

        // CREATE EXPENSE
        System.out.println("→ Creating test expense...");
        String expRes = client.insert("expenses",
                "{ \"description\": \"JUnit Expense\", " +
                "\"amount\": 10.55, " +
                "\"date\": \"2025-11-16\", " +
                "\"category_id\": " + categoryId + " }"
        );

        JSONArray expArr = new JSONArray(expRes);
        int expenseId = expArr.getJSONObject(0).getInt("id");
        System.out.println("✔ Expense created: ID = " + expenseId);

        // READ EXPENSES
        System.out.println("\n→ Fetching expenses...");
        String fetchRes = client.select("expenses");
        JSONArray expenses = new JSONArray(fetchRes);
        for (int i = 0; i < expenses.length(); i++) {
            System.out.println(expenses.getJSONObject(i));
        }

        // CLEANUP
        System.out.println("\n→ Cleaning test expense & category...");
        client.delete("expenses", expenseId);
        client.delete("categories", categoryId);

        System.out.println("✔ Cleanup complete!");
    }
}

