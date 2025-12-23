package org.luwrain.io.api.github;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.luwrain.io.api.github.models.Comment;
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

public class GitHubClient implements GitHubService {

    private static final String BASE_URL = "https://api.github.com";
    private final String token;
    private final HttpClient httpClient;
    private final Gson gson;

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
            String res = sendRequest("/user", "GET", null);
            return res.contains("login");
        } catch (GitHubException e) {
            // Если ошибка 401 (Unauthorized), значит токен неверный
            if (e.getStatusCode() == 401) return false;
            throw e; 
        }
    }

    @Override public List<Repo> getMyRepos()
    {
        String res = sendRequest("/user/repos?sort=updated&per_page=10", "GET", null);
        return parseList(res, new TypeToken<ArrayList<Repo>>(){}.getType());
    }

    @Override
    public List<Repo> search(String query) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String res = sendRequest("/search/repositories?q=" + encoded + "&per_page=5", "GET", null);
        JsonObject root = JsonParser.parseString(res).getAsJsonObject();
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
        String res = sendRequest("/repos/" + repoFullName + "/issues?state=open&per_page=10", "GET", null);
        return parseList(res, new TypeToken<ArrayList<Issue>>(){}.getType());
    }

    @Override
    public void createIssue(String repoFullName, String title, String text) {
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("body", text);
        sendRequest("/repos/" + repoFullName + "/issues", "POST", gson.toJson(json));
    }

    @Override
    public void closeIssue(String repoFullName, int issueNumber) {
        JsonObject json = new JsonObject();
        json.addProperty("state", "closed");
        
        String path = "/repos/" + repoFullName + "/issues/" + issueNumber;
        sendRequest(path, "PATCH", gson.toJson(json)); 
        System.out.println("Issue #" + issueNumber + " закрыто.");
    }

    @Override
    public List<Comment> getComments(String repoFullName, int issueNumber) {
        String path = "/repos/" + repoFullName + "/issues/" + issueNumber + "/comments";
        String res = sendRequest(path, "GET", null);
        return parseList(res, new TypeToken<ArrayList<Comment>>(){}.getType());
    }

    @Override
    public void postComment(String repoFullName, int issueNumber, String text) {
        JsonObject json = new JsonObject();
        json.addProperty("body", text);
        String path = "/repos/" + repoFullName + "/issues/" + issueNumber + "/comments";
        sendRequest(path, "POST", gson.toJson(json));
    }

    

    private String sendRequest(String path, String method, String jsonBody) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("User-Agent", "Java-GitHub-Component") 
                    .method(method, jsonBody == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));

            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new GitHubException("GitHub API Error: " + response.body(), response.statusCode());
            }
            return response.body();
        } catch (GitHubException e) {
            throw e; // Пробрасываем ошибку дальше
        } catch (Exception e) {
            throw new GitHubException("Connection failed: " + e.getMessage(), e);
        }
    }

    private <T> List<T> parseList(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
