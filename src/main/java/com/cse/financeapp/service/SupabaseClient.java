package com.cse.financeapp.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * SupabaseClient (Version B)
 * - Loads URL/API key from environment variables: SUPABASE_URL and SUPABASE_KEY
 * - Returns raw string responses (caller decides how to parse)
 * - Provides select, insert, upsert, patch, delete, deleteWhere
 */
public class SupabaseClient {

    private static final String SUPABASE_URL = System.getenv("SUPABASE_URL");
    private static final String SUPABASE_API_KEY = System.getenv("SUPABASE_KEY");

    private final HttpClient httpClient;

    public SupabaseClient() {
        if (SUPABASE_URL == null || SUPABASE_API_KEY == null) {
            throw new RuntimeException(
                "Missing environment variables SUPABASE_URL or SUPABASE_KEY. " +
                "Set them locally or in GitHub Actions as repository secrets."
            );
        }
        this.httpClient = HttpClient.newHttpClient();
    }

    private HttpRequest.Builder base(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(SUPABASE_URL + "/rest/v1/" + path))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                // prefer representation so insert/patch/delete return created/updated rows
                .header("Prefer", "return=representation");
    }

    private String send(HttpRequest req) throws Exception {
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        String body = resp.body();
        return body == null ? "" : body;
    }

    // SELECT all columns
    public String select(String table) throws Exception {
        HttpRequest req = base(table + "?select=*")
                .GET()
                .build();
        return send(req);
    }

    // INSERT (returns representation or error object)
    public String insert(String table, String jsonBody) throws Exception {
        HttpRequest req = base(table)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return send(req);
    }

    // UPSERT (merge duplicates) - caller must craft json and on_conflict in URL if needed
    public String upsert(String table, String jsonBody) throws Exception {
        HttpRequest req = base(table)
                .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Prefer", "resolution=merge-duplicates,return=representation")
                .build();
        return send(req);
    }

    // PATCH partial update by id (safe update)
    public String patch(String table, int id, String jsonBody) throws Exception {
        HttpRequest req = base(table + "?id=eq." + id)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return send(req);
    }

    // DELETE by id
    public String delete(String table, int id) throws Exception {
        HttpRequest req = base(table + "?id=eq." + id)
                .DELETE()
                .build();
        return send(req);
    }

    // DELETE by arbitrary WHERE column = value (value assumed simple, not quoted)
    // Caller should ensure proper formatting (quotes) if needed.
    public String deleteWhere(String table, String column, String value) throws Exception {
        // If the value contains spaces or special chars, caller should pass encoded/quoted value.
        HttpRequest req = base(table + "?" + column + "=eq." + value)
                .DELETE()
                .build();
        return send(req);
    }
}
