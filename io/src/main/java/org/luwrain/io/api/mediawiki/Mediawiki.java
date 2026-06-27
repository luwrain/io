// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.mediawiki;

import java.util.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import org.apache.logging.log4j.*;
import com.google.gson.*;

import static java.util.Objects.*;

/**
 * Client for the MediaWiki API that performs search queries.
 *
 * <p>This client communicates with a MediaWiki instance using its
 * {@code action=query&list=search} endpoint.  Responses are parsed from JSON
 * using {@code Gson}.  Each search result is returned as a {@link Page} object
 * with the page title, a plain-text snippet, and the API base URL.</p>
 *
 * <p>The constructor accepts the base URL of the MediaWiki API endpoint.
 * For English Wikipedia this is
 * {@code https://en.wikipedia.org/w/}.  The trailing slash, if missing,
 * is appended automatically.</p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Mediawiki wiki = new Mediawiki("https://en.wikipedia.org/w/");
 * List<Page> results = wiki.search("Dolphin");
 * for (Page p : results) {
 *     System.out.println(p.getTitle());
 * }
 * }</pre>
 *
 * @see Page
 */
public final class Mediawiki
{
    static private final int DEFAULT_SEARCH_LIMIT = 20;
    static private final Gson gson = new Gson();
    static private final Logger log = LogManager.getLogger();

    private final String baseUrl;
    private final HttpClient httpClient;

    /**
     * Creates a new MediaWiki API client.
     *
     * @param baseUrl the MediaWiki API base URL (e.g.
     *                {@code https://en.wikipedia.org/w/});
     *                must not be null or empty
     * @throws NullPointerException     if baseUrl is null
     * @throws IllegalArgumentException if baseUrl is empty
     */
    public Mediawiki(String baseUrl)
    {
	requireNonNull(baseUrl, "baseUrl can't be null");
	if (baseUrl.isEmpty())
	    throw new IllegalArgumentException("baseUrl can't be empty");
	this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
	this.httpClient = HttpClient.newBuilder()
	    .version(HttpClient.Version.HTTP_2)
	    .connectTimeout(Duration.ofSeconds(10))
	    .build();
    }

    /**
     * Searches the wiki for pages matching the given query.
     *
     * @param q the search query string; must not be null or empty
     * @return a list of matching pages (never null, may be empty)
     * @throws IOException          if a network or protocol error occurs
     * @throws NullPointerException if q is null
     */
    public List<Page> search(String q) throws IOException
    {
	requireNonNull(q, "q can't be null");
	if (q.isEmpty())
	    return Collections.emptyList();
	final String url = buildSearchUrl(q);
	log.trace("Searching using the following URL: {}", url);
	final HttpRequest request = HttpRequest.newBuilder()
 .uri(URI.create(url))
	.header("User-Agent", "LUWRAIN/2.0 (mailto:info@luwrain.org) Java/17")
		    .header("Accept", "application/json")
	    .GET()
	    .build();
	final HttpResponse<String> response;
	try {
	    response = httpClient.send(request, BodyHandlers.ofString());
	}
	catch (InterruptedException e) {
	    throw new IOException("Search request was interrupted", e);
	}
	if (response.statusCode() != 200)
	    throw new IOException("MediaWiki API returned HTTP " + response.statusCode() + " (" + url + ")");
	return parseSearchResponse(response.body());
    }

    /**
     * Builds the search API URL from the base URL and query string.
     *
     * @param q the search query
     * @return the full URL for the search request
     */
    private String buildSearchUrl(String q)
    {
	final StringBuilder sb = new StringBuilder();
	sb.append(baseUrl)
	    .append("api.php?action=query&list=search&format=json")
	//	    .append("&srlimit=").append(DEFAULT_SEARCH_LIMIT)
	    .append("&srsearch=")
	    .append(URLEncoder.encode(q, java.nio.charset.StandardCharsets.UTF_8));
	return sb.toString();
    }

    /**
     * Parses the JSON response body and extracts a list of {@link Page} objects.
     *
     * @param json the JSON response body
     * @return the list of parsed pages
     * @throws IOException if the JSON structure is unexpected
     */
    private List<Page> parseSearchResponse(String json) throws IOException
    {
	final JsonObject root;
	try {
	    root = JsonParser.parseString(json).getAsJsonObject();
	}
	catch (JsonParseException e) {
	    throw new IOException("Failed to parse JSON response from MediaWiki API", e);
	}
	final JsonObject query = root.getAsJsonObject("query");
	if (query == null)
	    throw new IOException("MediaWiki API response is missing the 'query' element");
	final JsonArray search = query.getAsJsonArray("search");
	if (search == null)
	    throw new IOException("MediaWiki API response is missing the 'search' element");
	final List<Page> res = new ArrayList<>();
	for (int i = 0; i < search.size(); ++i)
	{
	    final JsonObject item = search.get(i).getAsJsonObject();
	    final String title = getStringOrNull(item, "title");
	    if (title == null || title.isEmpty())
		continue;
	    final String snippet = getStringOrNull(item, "snippet");
	    final String comment = snippet != null ? stripTags(snippet) : "";
	    res.add(new Page(baseUrl, title, comment));
	}
	return res;
    }

    /**
     * Safely extracts a string property from a JSON object, returning
     * {@code null} if the property is missing or not a string.
     *
     * @param obj  the JSON object
     * @param name the property name
     * @return the string value, or {@code null}
     */
    static private String getStringOrNull(JsonObject obj, String name)
    {
	final JsonElement el = obj.get(name);
	if (el == null || el.isJsonNull())
	    return null;
	return el.getAsString();
    }

    /**
     * Removes HTML tags from a MediaWiki search snippet.
     * The API wraps matching terms in {@code <span class="searchmatch">} tags;
     * this method strips all {@code <span>} elements from the text.
     *
     * @param s the raw snippet text
     * @return the plain-text snippet
     */
    static private String stripTags(String s)
    {
	return s.replaceAll("</?span[^>]*>", "");
    }
}
