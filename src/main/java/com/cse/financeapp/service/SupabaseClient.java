package com.cse.financeapp.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Minimal Supabase REST client for basic CRUD (insert/select/update/delete).
 * Uses env vars: SUPABASE_URL and SUPABASE_KEY
 */
public class SupabaseClient {

    private static final String SUPABASE_URL = System.getenv("SUPABASE_URL");
    private static final String SUPABASE_API_KEY = System.getenv("SUPABASE_KEY");

    private final HttpClient client;

    public SupabaseClient() {
        if (SUPABASE_URL == null || SUPABASE_API_KEY == null) {
            throw new RuntimeException("Missing environment variables SUPABASE_URL or SUPABASE_KEY");
        }
        this.client = HttpClient.newHttpClient();
    }

    private String safe(String body) {
        return body == null ? "[]" : body.trim();
    }

    // SELECT: returns JSON (usually array)
    public String select(String table) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?select=*";
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return safe(resp.body());
    }

    // INSERT: returns representation when Prefer header set
    public String insert(String table, String jsonBody) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table;
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return safe(resp.body());
    }

    // UPDATE: PATCH to row by id, returns representation with Prefer
    public String update(String table, int id, String jsonBody) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?id=eq." + id;
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return safe(resp.body());
    }

    // DELETE by ID
    public String delete(String table, int id) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?id=eq." + id;
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return safe(resp.body());
    }
}
