package com.cse.financeapp.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SupabaseClient {

    private final String supabaseUrl;
    private final String apiKey;
    private final HttpClient client;

    // Base URL should look like: https://xyz.supabase.co/rest/v1/
    public SupabaseClient(String supabaseUrl, String apiKey) {
        if (!supabaseUrl.endsWith("/")) {
            supabaseUrl += "/";
        }
        this.supabaseUrl = supabaseUrl + "rest/v1/";
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    // GET with filtering (example: expenses?category=eq.Food)
    public String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(supabaseUrl + endpoint))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    // POST JSON -> inserts row
    public String post(String endpoint, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(supabaseUrl + endpoint))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")  // gets inserted value back
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    // DELETE with filters (example: expenses?id=eq.3)
    public String delete(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(supabaseUrl + endpoint))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}
