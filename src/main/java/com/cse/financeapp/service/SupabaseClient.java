package com.cse.financeapp.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SupabaseClient {

    // --- Your Supabase URL + Key ---
    private static final String SUPABASE_URL = 
            "https://ggjvorvnrrbqixszpemk.supabase.co/rest/v1/";

    private static final String SUPABASE_API_KEY =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdnanZvcnZucnJicWl4c3pwZW1rIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMyNDAzNzcsImV4cCI6MjA3ODgxNjM3N30.jPcgtyajOl0qM35XCPWAygmWeH2ecHMTMxBOCF5IVtU";

    private final HttpClient httpClient;

    public SupabaseClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // ---------------------------------------------------------
    // GENERIC SELECT
    // ---------------------------------------------------------
    public String select(String tableAndQuery) throws Exception {

        String url = SUPABASE_URL + tableAndQuery;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    // ---------------------------------------------------------
    // GENERIC INSERT
    // ---------------------------------------------------------
    public String insert(String table, String jsonBody) throws Exception {

        String url = SUPABASE_URL + table;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    // ---------------------------------------------------------
    // GENERIC UPDATE (PATCH)
    // ---------------------------------------------------------
    public String update(String table, int id, String jsonBody) throws Exception {

        String url = SUPABASE_URL + table + "?id=eq." + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    // ---------------------------------------------------------
    // GENERIC DELETE
    // ---------------------------------------------------------
    public String delete(String table, int id) throws Exception {

        String url = SUPABASE_URL + table + "?id=eq." + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .DELETE()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
