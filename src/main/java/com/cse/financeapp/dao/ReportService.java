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

    // -------------------------------------------------------------
    // REPORT: TOTAL SPENT BY CATEGORY
    // -------------------------------------------------------------
    public List<Report> getTotalSpentByCategory() {
        List<Report> list = new ArrayList<>();

        try {
            // Correct RPC call — no /rest/v1/
            String resp = client.post("rpc/total_spent_by_category", "{}");

            JSONArray arr = new JSONArray(resp);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                list.add(new Report(
                        o.getString("category"),
                        o.getDouble("total_spent")
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to generate report");
            e.printStackTrace();
        }

        return list;
    }
}
