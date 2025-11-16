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
    // Generic GET (Select)
    // ---------------------------------------------------------
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

        return response.body();
    }

    // ---------------------------------------------------------
    // Generic INSERT
    // ---------------------------------------------------------
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

        return response.body();
    }

    // ---------------------------------------------------------
    // Generic DELETE by ID
    // ---------------------------------------------------------
    public String delete(String table, int id) throws Exception {
        String url = SUPABASE_URL + "/rest/v1/" + table + "?id=eq." + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}

