// SPDX-License-Identifier: BUSL-1.1
// Copyright 2025 Fedor Spirin <fspirin8@gmail.com>

package org.luwrain.io.api.github;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.luwrain.io.api.github.models.Comment;
import org.luwrain.io.api.github.models.Commit;
import org.luwrain.io.api.github.models.Issue;
import org.luwrain.io.api.github.models.Repo;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * HTTP client implementation of {@link GitHubService} that communicates
 * with the GitHub REST API v3.
 *
 * <p>This client uses Bearer token authentication and parses JSON responses
 * using Gson. All API calls are made to {@code https://api.github.com}.</p>
 */
public class GitHubClient implements GitHubService {

    private static final String BASE_URL = "https://api.github.com";
    private final String token;
    private final HttpClient httpClient;
    private final Gson gson;

    /**
     * Creates a new GitHub client with the specified personal access token.
     *
     * @param token GitHub personal access token; must not be null or empty
     * @throws IllegalArgumentException if the token is null or empty
     */
    public GitHubClient(String token)
    {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be empty");
        }
        this.token = token;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    @Override public boolean connect()
    {
        try {
            final var res = sendRequest("/user", "GET", null);
            return res.contains("login");
        } catch (GitHubException e) {
            // A 401 (Unauthorized) status means the token is invalid
            if (e.getStatusCode() == 401) return false;
            throw e;
        }
    }

    @Override public List<Repo> getMyRepos()
    {
        final var res = sendRequest("/user/repos?sort=updated&per_page=10", "GET", null);
        return parseList(res, new TypeToken<ArrayList<Repo>>(){}.getType());
    }

    @Override
    public List<Repo> search(String query) {
        final var encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        final var res = sendRequest("/search/repositories?q=" + encoded + "&per_page=5", "GET", null);
        final var root = JsonParser.parseString(res).getAsJsonObject();
        return parseList(root.get("items").toString(), new TypeToken<ArrayList<Repo>>(){}.getType());
    }

    @Override
    public void joinRepo(String repoFullName) {
        sendRequest("/user/starred/" + repoFullName, "PUT", null);
    }

    @Override
    public void leaveRepo(String repoFullName) {
        sendRequest("/user/starred/" + repoFullName, "DELETE", null);
    }

    @Override
    public List<Issue> getIssues(String repoFullName) {
        final var res = sendRequest("/repos/" + repoFullName + "/issues?state=open&per_page=10", "GET", null);
        return parseList(res, new TypeToken<ArrayList<Issue>>(){}.getType());
    }

    @Override
    public void createIssue(String repoFullName, String title, String text) {
        final var json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("body", text);
        sendRequest("/repos/" + repoFullName + "/issues", "POST", gson.toJson(json));
    }

    @Override
    public void closeIssue(String repoFullName, int issueNumber) {
        final var json = new JsonObject();
        json.addProperty("state", "closed");
        final var path = "/repos/" + repoFullName + "/issues/" + issueNumber;
        sendRequest(path, "PATCH", gson.toJson(json));
    }

    @Override
    public List<Comment> getComments(String repoFullName, int issueNumber) {
        final var path = "/repos/" + repoFullName + "/issues/" + issueNumber + "/comments";
        final var res = sendRequest(path, "GET", null);
        return parseList(res, new TypeToken<ArrayList<Comment>>(){}.getType());
    }

    @Override
    public void postComment(String repoFullName, int issueNumber, String text) {
        final var json = new JsonObject();
        json.addProperty("body", text);
        final var path = "/repos/" + repoFullName + "/issues/" + issueNumber + "/comments";
        sendRequest(path, "POST", gson.toJson(json));
    }

    @Override
    public List<Commit> getCommits(String repoFullName)
    {
        final var path = "/repos/" + repoFullName + "/commits?per_page=10";
        final var res = sendRequest(path, "GET", null);
        return parseList(res, new TypeToken<ArrayList<Commit>>(){}.getType());
    }

    /**
     * Sends an HTTP request to the GitHub API.
     *
     * @param path     the API path relative to {@link #BASE_URL}
     * @param method   the HTTP method (GET, POST, PUT, PATCH, DELETE)
     * @param jsonBody the JSON request body, or null for no body
     * @return the response body as a string
     * @throws GitHubException if the HTTP status is 400 or higher, or if a network error occurs
     */
    private String sendRequest(String path, String method, String jsonBody) {
        try {
            final var builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("User-Agent", "Java-GitHub-Component")
                    .method(method, jsonBody == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));

            final var response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new GitHubException("GitHub API Error: " + response.body(), response.statusCode());
            }
            return response.body();
        } catch (GitHubException e) {
            throw e; // Re-throw without wrapping
        } catch (Exception e) {
            throw new GitHubException("Connection failed: " + e.getMessage(), e);
        }
    }

    /**
     * Parses a JSON array string into a list of objects of the given type.
     *
     * @param json the JSON array string
     * @param type the target list type
     * @param <T>  the element type
     * @return the parsed list
     */
    private <T> List<T> parseList(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
