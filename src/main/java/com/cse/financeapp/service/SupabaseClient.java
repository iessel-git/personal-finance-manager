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

    // ---------------------------------------------------------------------
    // SAFETY WRAPPER — ensures response is always an array ("[]")
    // ---------------------------------------------------------------------
    public String safeArrayResponse(String response) {
        if (response == null) return "[]";
        String trimmed = response.trim();

        // Supabase error responses come as JSON OBJECTS. Convert to empty array.
        if (trimmed.startsWith("{")) {
            System.out.println("⚠ Supabase returned OBJECT instead of ARRAY: " + trimmed);
            return "[]";
        }

        return trimmed;
    }

    // ---------------------------------------------------------------------
    // SELECT TABLE
    // ---------------------------------------------------------------------
    public String select(String table) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?select=*";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }

    // ---------------------------------------------------------------------
    // INSERT
    // ---------------------------------------------------------------------
    public String insert(String table, String jsonBody) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }

    // ---------------------------------------------------------------------
    // DELETE by ID
    // ---------------------------------------------------------------------
    public String delete(String table, int id) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?id=eq." + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .DELETE()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }

    // ---------------------------------------------------------------------
    // NEW: DELETE by ANY COLUMN
    // ---------------------------------------------------------------------
    public String deleteWhere(String table, String column, String value) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?" + column + "=eq." + value;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .DELETE()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }

    // ---------------------------------------------------------------------
    // UPSERT
    // ---------------------------------------------------------------------
    public String upsert(String table, String jsonBody) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?on_conflict=id";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return safeArrayResponse(response.body());
    }
}
