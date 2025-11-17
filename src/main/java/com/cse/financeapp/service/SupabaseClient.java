package com.cse.financeapp.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SupabaseClient {

    private static final String SUPABASE_URL = System.getenv("SUPABASE_URL");
    private static final String SUPABASE_API_KEY = System.getenv("SUPABASE_KEY");

    private final HttpClient client;

    public SupabaseClient() {
        this.client = HttpClient.newHttpClient();

        if (SUPABASE_URL == null || SUPABASE_API_KEY == null) {
            throw new RuntimeException("❌ Missing environment variables: SUPABASE_URL or SUPABASE_KEY");
        }
    }

    // ---------------------------------------------------------
    // RESPONSE SAFETY WRAPPER — always returns valid "[]"
    // ---------------------------------------------------------
    public String safeArrayResponse(String response) {
        if (response == null) return "[]";

        String trimmed = response.trim();

        // If Supabase returns an error object, treat it as empty results
        if (trimmed.startsWith("{")) {
            System.out.println("⚠ Supabase returned OBJECT instead of ARRAY: " + trimmed);
            return "[]";
        }

        return trimmed;
    }

    // ---------------------------------------------------------
    // SELECT
    // ---------------------------------------------------------
    public String select(String table) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?select=*";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }

    // ---------------------------------------------------------
    // INSERT (always returns ARRAY)
    // ---------------------------------------------------------
    public String insert(String table, String jsonBody) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }

    // ---------------------------------------------------------
    // DELETE by ID (always returns ARRAY)
    // ---------------------------------------------------------
    public String delete(String table, int id) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?id=eq." + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Prefer", "return=representation")
                .DELETE()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }

    // ---------------------------------------------------------
    // DELETE by NAME (for cleanup)
    // ---------------------------------------------------------
    public String deleteWhere(String table, String column, String value) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?" + column + "=eq." + value;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Prefer", "return=representation")
                .DELETE()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }

    // ---------------------------------------------------------
    // UPSERT (always returns ARRAY)
    // ---------------------------------------------------------
    public String upsert(String table, String jsonBody) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?on_conflict=id";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }
}
