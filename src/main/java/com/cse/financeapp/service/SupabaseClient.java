package com.cse.financeapp.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * SupabaseClient (Version B)
 * - Loads URL/API key from environment variables
 * - Returns raw string responses
 * - Provides GET, INSERT, UPSERT, PATCH, DELETE
 */
public class SupabaseClient {

    // Load from environment variables
    private static final String SUPABASE_URL = System.getenv("SUPABASE_URL");
    private static final String SUPABASE_API_KEY = System.getenv("SUPABASE_KEY");

    private final HttpClient httpClient;

    public SupabaseClient() {
        if (SUPABASE_URL == null || SUPABASE_API_KEY == null) {
            throw new RuntimeException("""
                Missing SUPABASE_URL or SUPABASE_KEY environment variables.
                Ensure GitHub Actions uses:
                env:
                  SUPABASE_URL: ${{ secrets.SUPABASE_URL }}
                  SUPABASE_KEY: ${{ secrets.SUPABASE_KEY }}
            """);
        }

        this.httpClient = HttpClient.newHttpClient();
    }

    // Base request builder
    private HttpRequest.Builder base(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(SUPABASE_URL + "/rest/v1/" + path))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation");
    }

    private String send(HttpRequest request) throws Exception {
        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = resp.body();
        return body == null ? "" : body;
    }

    // ============================================================
    // SELECT
    // ============================================================
    public String select(String table) throws Exception {
        HttpRequest request = base(table + "?select=*")
                .GET()
                .build();

        return send(request);
    }

    // ============================================================
    // INSERT
    // ============================================================
    public String insert(String table, String json) throws Exception {
        HttpRequest request = base(table)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return send(request);
    }

    // ============================================================
    // UPSERT (full replace unless PATCH is used)
    // ============================================================
    public String upsert(String table, String json) throws Exception {
        HttpRequest request = base(table)
                .method("POST", HttpRequest.BodyPublishers.ofString(json))
                .header("Prefer", "resolution=merge-duplicates,return=representation")
                .build();

        return send(request);
    }

    // ============================================================
    // PATCH — safe partial update
    // ============================================================
    public String patch(String table, int id, String json) throws Exception {
        HttpRequest request = base(table + "?id=eq." + id)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        return send(request);
    }

    // ============================================================
    // DELETE
    // ============================================================
    public String delete(String table, int id) throws Exception {
        HttpRequest request = base(table + "?id=eq." + id)
                .DELETE()
                .build();

        return send(request);
    }
}
