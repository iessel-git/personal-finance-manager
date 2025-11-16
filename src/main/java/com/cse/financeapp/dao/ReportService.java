package com.cse.financeapp.dao;

import com.cse.financeapp.models.Report;
import com.cse.financeapp.service.SupabaseClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportService {

    private final SupabaseClient client;

    public ReportService(SupabaseClient client) {
        this.client = client;
    }

    public List<Report> getTotalSpentByCategory() {
        List<Report> list = new ArrayList<>();

        try {
            // Supabase allows SELECT with aggregation using query params
            String response = client.select("expenses?select=category_id,amount");
            JSONArray arr = new JSONArray(response);

            // Aggregate totals manually
            JSONObject totals = new JSONObject();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                int catId = o.getInt("category_id");
                double amount = o.getDouble("amount");

                totals.put(String.valueOf(catId),
                        totals.optDouble(String.valueOf(catId), 0) + amount);
            }

            // Convert to Report objects
            for (String key : totals.keySet()) {
                list.add(new Report(
                        "Category " + key,
                        totals.getDouble(key)
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to generate report");
            e.printStackTrace();
        }

        return list;
    }
}
