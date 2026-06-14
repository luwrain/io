// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs.api;

import java.util.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

import org.luwrain.app.bs.model.*;

import static java.util.Objects.*;

public final class BlueSkyApi
{
    static private final Gson GSON = new Gson();

    static public final String DEFAULT_ENDPOINT = "https://bsky.social";

    public enum AuthResult
    {
	SUCCESS,
	INVALID_CREDENTIALS,
	NETWORK_ERROR,
	UNKNOWN_ERROR
    }

    private final String endpoint;
    private final HttpClient httpClient;

    public BlueSkyApi()
    {
	this(DEFAULT_ENDPOINT);
    }

    public BlueSkyApi(String endpoint)
    {
	this.endpoint = requireNonNull(endpoint);
	this.httpClient = HttpClient.newHttpClient();
    }

    public String getEndpoint()
    {
	return endpoint;
    }

    // ---- Регистрация ----

    public AuthData createAccount(String email, String handle, String password,
				  String inviteCode) throws ApiException
    {
	final var body = new JsonObject();
	body.addProperty("email", email);
	body.addProperty("handle", handle);
	body.addProperty("password", password);
	if (inviteCode != null && !inviteCode.trim().isEmpty())
	    body.addProperty("inviteCode", inviteCode.trim());

	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(endpoint + "/xrpc/com.atproto.server.createAccount"))
	    .header("Content-Type", "application/json")
	    .POST(BodyPublishers.ofString(body.toString()))
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при регистрации: " + e.getMessage(), e);
	}

	if (response.statusCode() == 200)
	{
	    final var json = GSON.fromJson(response.body(), JsonObject.class);
	    final var authData = new AuthData();
	    authData.setDid(getString(json, "did"));
	    authData.setHandle(getString(json, "handle"));
	    authData.setAccessJwt(getString(json, "accessJwt"));
	    authData.setRefreshJwt(getString(json, "refreshJwt"));
	    authData.setEmail(getString(json, "email"));
	    if (json.has("emailConfirmed"))
		authData.setEmailConfirmed(json.get("emailConfirmed").getAsBoolean());
	    return authData;
	}

	throw new ApiException(response.statusCode(),
			       "Ошибка регистрации: " + extractErrorMessage(response.body()));
    }

    // ---- Авторизация (создание сессии) ----

    public AuthData createSession(String identifier, String password) throws ApiException
    {
	final var body = new JsonObject();
	body.addProperty("identifier", identifier);
	body.addProperty("password", password);

	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(endpoint + "/xrpc/com.atproto.server.createSession"))
	    .header("Content-Type", "application/json")
	    .POST(BodyPublishers.ofString(body.toString()))
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при авторизации: " + e.getMessage(), e);
	}

	if (response.statusCode() == 200)
	{
	    final var json = GSON.fromJson(response.body(), JsonObject.class);
	    final var authData = new AuthData();
	    authData.setDid(getString(json, "did"));
	    authData.setHandle(getString(json, "handle"));
	    authData.setAccessJwt(getString(json, "accessJwt"));
	    authData.setRefreshJwt(getString(json, "refreshJwt"));
	    authData.setEmail(getString(json, "email"));
	    if (json.has("emailConfirmed"))
		authData.setEmailConfirmed(json.get("emailConfirmed").getAsBoolean());
	    return authData;
	}

	if (response.statusCode() == 401)
	    throw new ApiException(401, "Неверный идентификатор или пароль");

	throw new ApiException(response.statusCode(),
			       "Ошибка авторизации: " + extractErrorMessage(response.body()));
    }

    // ---- Обновление сессии ----

    public AuthData refreshSession(String refreshJwt) throws ApiException
    {
	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(endpoint + "/xrpc/com.atproto.server.refreshSession"))
	    .header("Authorization", "Bearer " + refreshJwt)
	    .POST(BodyPublishers.noBody())
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при обновлении сессии: " + e.getMessage(), e);
	}

	if (response.statusCode() == 200)
	{
	    final var json = GSON.fromJson(response.body(), JsonObject.class);
	    final var authData = new AuthData();
	    authData.setDid(getString(json, "did"));
	    authData.setHandle(getString(json, "handle"));
	    authData.setAccessJwt(getString(json, "accessJwt"));
	    authData.setRefreshJwt(getString(json, "refreshJwt"));
	    return authData;
	}

	throw new ApiException(response.statusCode(),
			       "Ошибка обновления сессии: " + extractErrorMessage(response.body()));
    }

    // ---- Публикация сообщения ----

    public CreatedRecord createPost(String did, String accessJwt, String text) throws ApiException
    {
	if (text == null || text.trim().isEmpty())
	    throw new ApiException("Текст сообщения не может быть пустым");
	if (text.length() > 300)
	    throw new ApiException("Текст сообщения превышает 300 символов");

	final var recordObj = new JsonObject();
	recordObj.addProperty("$type", "app.bsky.feed.post");
	recordObj.addProperty("text", text);
	recordObj.addProperty("createdAt", nowUtc());

	final var body = new JsonObject();
	body.addProperty("repo", did);
	body.addProperty("collection", "app.bsky.feed.post");
	body.add("record", recordObj);

	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(endpoint + "/xrpc/com.atproto.repo.createRecord"))
	    .header("Content-Type", "application/json")
	    .header("Authorization", "Bearer " + accessJwt)
	    .POST(BodyPublishers.ofString(body.toString()))
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при публикации: " + e.getMessage(), e);
	}

	if (response.statusCode() == 200)
	{
	    final var json = GSON.fromJson(response.body(), JsonObject.class);
	    return new CreatedRecord(
		getString(json, "uri"),
		getString(json, "cid")
	    );
	}

	throw new ApiException(response.statusCode(),
			       "Ошибка публикации: " + extractErrorMessage(response.body()));
    }

    // ---- Лента ----

    public TimelineResponse getTimeline(String did, String accessJwt,
					int limit, String cursor) throws ApiException
    {
	final var uri = endpoint + "/xrpc/app.bsky.feed.getTimeline"
	    + "?algorithm=reverse-chronological"
	    + "&limit=" + Math.max(1, Math.min(100, limit > 0 ? limit : 50));
	final var fullUri = cursor != null && !cursor.isEmpty()
	    ? uri + "&cursor=" + URLEncoder.encode(cursor, java.nio.charset.StandardCharsets.UTF_8)
	    : uri;

	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(fullUri))
	    .header("Authorization", "Bearer " + accessJwt)
	    .GET()
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при получении ленты: " + e.getMessage(), e);
	}

	if (response.statusCode() == 200)
	    return parseTimelineResponse(response.body());

	throw new ApiException(response.statusCode(),
			       "Ошибка получения ленты: " + extractErrorMessage(response.body()));
    }

    // ---- Лента автора ----

    public TimelineResponse getAuthorFeed(String actorDid, String accessJwt,
					  int limit, String cursor) throws ApiException
    {
	final var uri = endpoint + "/xrpc/app.bsky.feed.getAuthorFeed"
	    + "?actor=" + actorDid
	    + "&limit=" + Math.max(1, Math.min(100, limit > 0 ? limit : 50));
	final var fullUri = cursor != null && !cursor.isEmpty()
	    ? uri + "&cursor=" + URLEncoder.encode(cursor, java.nio.charset.StandardCharsets.UTF_8)
	    : uri;

	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(fullUri))
	    .header("Authorization", "Bearer " + accessJwt)
	    .GET()
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при получении ленты автора: " + e.getMessage(), e);
	}

	if (response.statusCode() == 200)
	    return parseTimelineResponse(response.body());

	throw new ApiException(response.statusCode(),
			       "Ошибка получения ленты автора: " + extractErrorMessage(response.body()));
    }

    // ---- Подписки (follows) ----

    public FollowsResponse getFollows(String actorDid, String accessJwt,
				      int limit, String cursor) throws ApiException
    {
	final var uri = endpoint + "/xrpc/app.bsky.graph.getFollows"
	    + "?actor=" + actorDid
	    + "&limit=" + Math.max(1, Math.min(100, limit > 0 ? limit : 50));
	final var fullUri = cursor != null && !cursor.isEmpty()
	    ? uri + "&cursor=" + URLEncoder.encode(cursor, java.nio.charset.StandardCharsets.UTF_8)
	    : uri;

	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(fullUri))
	    .header("Authorization", "Bearer " + accessJwt)
	    .GET()
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при получении подписок: " + e.getMessage(), e);
	}

	if (response.statusCode() == 200)
	    return parseFollowsResponse(response.body());

	throw new ApiException(response.statusCode(),
			       "Ошибка получения подписок: " + extractErrorMessage(response.body()));
    }

    // ---- Подписчики (followers) ----

    public FollowsResponse getFollowers(String actorDid, String accessJwt,
					int limit, String cursor) throws ApiException
    {
	final var uri = endpoint + "/xrpc/app.bsky.graph.getFollowers"
	    + "?actor=" + actorDid
	    + "&limit=" + Math.max(1, Math.min(100, limit > 0 ? limit : 50));
	final var fullUri = cursor != null && !cursor.isEmpty()
	    ? uri + "&cursor=" + URLEncoder.encode(cursor, java.nio.charset.StandardCharsets.UTF_8)
	    : uri;

	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(fullUri))
	    .header("Authorization", "Bearer " + accessJwt)
	    .GET()
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при получении подписчиков: " + e.getMessage(), e);
	}

	if (response.statusCode() == 200)
	    return parseFollowsResponse(response.body());

	throw new ApiException(response.statusCode(),
			       "Ошибка получения подписчиков: " + extractErrorMessage(response.body()));
    }

    // ---- Удаление подписки ----

    public void deleteFollow(String repoDid, String accessJwt,
			     String followUri) throws ApiException
    {
	final var body = new JsonObject();
	body.addProperty("repo", repoDid);
	body.addProperty("collection", "app.bsky.graph.follow");
	body.addProperty("rkey", extractRkey(followUri));

	final var request = HttpRequest.newBuilder()
	    .uri(URI.create(endpoint + "/xrpc/com.atproto.repo.deleteRecord"))
	    .header("Content-Type", "application/json")
	    .header("Authorization", "Bearer " + accessJwt)
	    .method("DELETE", BodyPublishers.ofString(body.toString()))
	    .build();

	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (IOException | InterruptedException e) {
	    throw new ApiException("Ошибка сети при удалении подписки: " + e.getMessage(), e);
	}

	if (response.statusCode() != 200)
	    throw new ApiException(response.statusCode(),
				   "Ошибка удаления подписки: " + extractErrorMessage(response.body()));
    }

    // ---- Вспомогательные методы ----

    static String nowUtc()
    {
	final var now = java.time.Instant.now();
	return now.toString().replace("Z", "Z"); // ISO-8601 с Z на конце
    }

    static private String getString(JsonObject json, String key)
    {
	if (json == null || !json.has(key) || json.get(key).isJsonNull())
	    return null;
	return json.get(key).getAsString();
    }

    static private String extractErrorMessage(String responseBody)
    {
	if (responseBody == null || responseBody.trim().isEmpty())
	    return "Неизвестная ошибка";
	try {
	    final var json = GSON.fromJson(responseBody, JsonObject.class);
	    if (json.has("message"))
		return json.get("message").getAsString();
	    if (json.has("error"))
		return json.get("error").getAsString();
	    return responseBody;
	}
	catch (Exception e) {
	    return responseBody;
	}
    }

    static private String extractRkey(String uri)
    {
	if (uri == null)
	    return "";
	final var idx = uri.lastIndexOf('/');
	if (idx >= 0 && idx + 1 < uri.length())
	    return uri.substring(idx + 1);
	return uri;
    }

    // ---- Разбор ответов API ----

    TimelineResponse parseTimelineResponse(String jsonBody)
    {
	final var json = GSON.fromJson(jsonBody, JsonObject.class);
	final var records = new ArrayList<Record>();
	final var feedArray = json.getAsJsonArray("feed");
	if (feedArray != null)
	{
	    for (final var element : feedArray)
	    {
		final var feedItem = element.getAsJsonObject();
		final var post = feedItem.getAsJsonObject("post");
		if (post == null)
		    continue;
		records.add(convertPostToRecord(post));
	    }
	}
	final String cursor = json.has("cursor") && !json.get("cursor").isJsonNull()
	    ? json.get("cursor").getAsString() : null;
	return new TimelineResponse(records, cursor);
    }

    FollowsResponse parseFollowsResponse(String jsonBody)
    {
	final var json = GSON.fromJson(jsonBody, JsonObject.class);
	final var followings = new ArrayList<Following>();
	final var followsArray = json.getAsJsonArray("follows");
	if (followsArray != null)
	{
	    for (final var element : followsArray)
	    {
		final var f = element.getAsJsonObject();
		final var following = new Following();
		following.setDid(getString(f, "did"));
		following.setHandle(getString(f, "handle"));
		following.setDisplayName(getString(f, "displayName"));
		following.setAvatar(getString(f, "avatar"));
		following.setDescription(getString(f, "description"));
		following.setCreatedAt(getString(f, "createdAt"));
		followings.add(following);
	    }
	}
	final String cursor = json.has("cursor") && !json.get("cursor").isJsonNull()
	    ? json.get("cursor").getAsString() : null;
	return new FollowsResponse(followings, cursor);
    }

    Record convertPostToRecord(JsonObject post)
    {
	final var record = new Record();
	record.setUri(getString(post, "uri"));
	record.setCid(getString(post, "cid"));

	final var author = post.getAsJsonObject("author");
	if (author != null)
	{
	    record.setAuthorDid(getString(author, "did"));
	    record.setAuthorHandle(getString(author, "handle"));
	    record.setAuthorDisplayName(getString(author, "displayName"));
	    record.setAuthorAvatar(getString(author, "avatar"));
	}

	final var rec = post.getAsJsonObject("record");
	if (rec != null)
	{
	    record.setText(getString(rec, "text"));
	    record.setCreatedAt(getString(rec, "createdAt"));
	}

	if (post.has("replyCount") && !post.get("replyCount").isJsonNull())
	    record.setReplyCount(post.get("replyCount").getAsInt());
	if (post.has("repostCount") && !post.get("repostCount").isJsonNull())
	    record.setRepostCount(post.get("repostCount").getAsInt());
	if (post.has("likeCount") && !post.get("likeCount").isJsonNull())
	    record.setLikeCount(post.get("likeCount").getAsInt());
	if (post.has("quoteCount") && !post.get("quoteCount").isJsonNull())
	    record.setQuoteCount(post.get("quoteCount").getAsInt());

	// Вложенный ответ
	final var reply = post.getAsJsonObject("reply");
	if (reply != null && reply.has("parent"))
	{
	    final var parent = reply.getAsJsonObject("parent");
	    if (parent != null)
		record.setReplyTo(convertPostToRecord(parent));
	}

	// Цитирование
	if (post.has("embed") && !post.get("embed").isJsonNull())
	{
	    final var embed = post.getAsJsonObject("embed");
	    if (embed != null && "app.bsky.embed.record#view".equals(getString(embed, "$type")))
	    {
		final var embedRecord = embed.getAsJsonObject("record");
		if (embedRecord != null)
		    record.setQuoteRecord(convertPostToRecord(embedRecord));
	    }
	}

	return record;
    }
}
