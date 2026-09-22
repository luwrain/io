// SPDX-License-Identifier: BUSL-1.1
// Copyright 2025 Fedor Spirin <fspirin8@gmail.com>

package org.luwrain.io.api.github;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.luwrain.io.api.github.models.Comment;
import org.luwrain.io.api.github.models.Commit;
import org.luwrain.io.api.github.models.Issue;
import org.luwrain.io.api.github.models.Repo;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HTTP client implementation of {@link GitHubService} that communicates
 * with the GitHub REST API v3.
 *
 * <p>This client uses Bearer token authentication and parses JSON responses
 * using Gson. All API calls are made to {@code https://api.github.com} and
 * are executed with OkHttp.</p>
 */
public class GitHubClient implements GitHubService
{
    static private final String BASE_URL = "https://api.github.com";
    static private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String token;
    private final OkHttpClient httpClient;
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
	this.httpClient = new OkHttpClient.Builder()
	        .connectTimeout(10, TimeUnit.SECONDS)
	        .readTimeout(30, TimeUnit.SECONDS)
	        .build();
	this.gson = new Gson();
    }

    @Override public boolean connect()
    {
	try {
	    final var res = sendRequest("/user", "GET", null);
	    final JsonObject root = JsonParser.parseString(res).getAsJsonObject();
	    return root.has("login");
	}
	catch(GitHubException e)
	{
	    // A 401 (Unauthorized) status means the token is invalid.
	    if (e.getStatusCode() == 401)
		return false;
	    throw e;
	}
    }

    @Override public List<Repo> getMyRepos()
    {
	final var res = sendRequest("/user/repos?sort=updated&per_page=10", "GET", null);
	return parseList(res, new TypeToken<ArrayList<Repo>>(){}.getType());
    }

    @Override public List<Repo> search(String query)
    {
	final var encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
	final var res = sendRequest("/search/repositories?q=" + encoded + "&per_page=10", "GET", null);
	final var root = JsonParser.parseString(res).getAsJsonObject();
	return parseList(root.get("items").toString(), new TypeToken<ArrayList<Repo>>(){}.getType());
    }

    @Override public void joinRepo(String repoFullName)
    {
	sendRequest("/user/starred/" + repoFullName, "PUT", null);
    }

    @Override public void leaveRepo(String repoFullName)
    {
	sendRequest("/user/starred/" + repoFullName, "DELETE", null);
    }

    @Override public List<Issue> getIssues(String repoFullName)
    {
	final var res = sendRequest("/repos/" + repoFullName + "/issues?state=open&per_page=10", "GET", null);
	return parseList(res, new TypeToken<ArrayList<Issue>>(){}.getType());
    }

    @Override public List<Issue> getPullRequests(String repoFullName)
    {
	final var res = sendRequest("/repos/" + repoFullName + "/pulls?state=open&per_page=10", "GET", null);
	return parseList(res, new TypeToken<ArrayList<Issue>>(){}.getType());
    }

    @Override public void createIssue(String repoFullName, String title, String text)
    {
	final var json = new JsonObject();
	json.addProperty("title", title);
	json.addProperty("body", text);
	sendRequest("/repos/" + repoFullName + "/issues", "POST", gson.toJson(json));
    }

    @Override public void closeIssue(String repoFullName, int issueNumber)
    {
	final var json = new JsonObject();
	json.addProperty("state", "closed");
	final var path = "/repos/" + repoFullName + "/issues/" + issueNumber;
	sendRequest(path, "PATCH", gson.toJson(json));
    }

    @Override public List<Comment> getComments(String repoFullName, int issueNumber)
    {
	final var path = "/repos/" + repoFullName + "/issues/" + issueNumber + "/comments";
	final var res = sendRequest(path, "GET", null);
	return parseList(res, new TypeToken<ArrayList<Comment>>(){}.getType());
    }

    @Override public void postComment(String repoFullName, int issueNumber, String text)
    {
	final var json = new JsonObject();
	json.addProperty("body", text);
	final var path = "/repos/" + repoFullName + "/issues/" + issueNumber + "/comments";
	sendRequest(path, "POST", gson.toJson(json));
    }

    @Override public List<Commit> getCommits(String repoFullName)
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
    private String sendRequest(String path, String method, String jsonBody)
    {
	final RequestBody body = jsonBody != null?RequestBody.create(jsonBody, JSON):null;
	final var request = new Request.Builder()
	        .url(BASE_URL + path)
	        .header("Authorization", "Bearer " + token)
	        .header("Accept", "application/vnd.github.v3+json")
	        .header("User-Agent", "LUWRAIN-GitHub-Client")
	        .method(method, body)
	        .build();
	try (Response response = httpClient.newCall(request).execute()) {
	    final String responseBody = response.body() != null?response.body().string():"";
	    if (!response.isSuccessful())
		throw new GitHubException("GitHub API error: " + responseBody, response.code());
	    return responseBody;
	}
	catch(GitHubException e)
	{
	    throw e;
	}
	catch(Exception e)
	{
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
    private <T> List<T> parseList(String json, Type type)
    {
	return gson.fromJson(json, type);
    }
}