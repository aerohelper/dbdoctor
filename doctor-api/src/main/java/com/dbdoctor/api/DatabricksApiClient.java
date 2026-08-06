package com.dbdoctor.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Thin wrapper around the Databricks REST API using the JDK's {@link HttpClient}.
 * Handles base URL and bearer-token authentication; individual check implementations
 * build on top of this for specific API calls.
 */
public class DatabricksApiClient {

    private final HttpClient httpClient;
    private final String baseUrl;
    private final String token;

    public DatabricksApiClient(String baseUrl, String token) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.token = token;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Performs a GET request against the given API path (e.g. {@code /api/2.0/clusters/list}).
     */
    public HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
