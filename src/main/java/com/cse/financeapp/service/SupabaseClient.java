package com.cse.financeapp.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * SupabaseClient (Version B)
 * - Returns raw response bodies (String).
 * - Adds Prefer: return=representation for insert/upsert/delete so Supabase returns rows when possible.
 * - Caller is responsible for detecting error objects vs arrays.
 */
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

    // Generic SELECT: returns raw response (usually a JSON array)
    public String select(String table) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?select=*";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Generic INSERT: returns raw response (should be array with inserted representation)
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

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Generic UPSERT (on_conflict=id): returns raw response
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

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Generic DELETE by ID: returns raw response (with return=representation will return deleted row(s))
    public String delete(String table, int id) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?id=eq." + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Prefer", "return=representation")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Convenience: delete where column = value (value must be url-safe; for strings, caller may need to not include quotes)
    public String deleteWhere(String table, String column, String value) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?" + column + "=eq." + value;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Prefer", "return=representation")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
