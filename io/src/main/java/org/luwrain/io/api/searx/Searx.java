
package org.luwrain.io.api.searx;

import java.util.*;
import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.*;

public class Searx
{
    static private final Logger log = LogManager.getLogger();
    static private final Gson gson = new Gson();

    final String endpoint;

    public Searx(String endpoint)
    {
	this.endpoint = requireNonNull(endpoint, "endpoint can't be null");
	if (endpoint.isEmpty())
	    throw new IllegalArgumentException("endpoint can't be empty");
    }

    public Response request(String query) throws IOException
    {
        final String
	requestMethod = "GET",
	url = endpoint,
	payload = "q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&format=json";
	log.trace("Searx request: url=" + url + ", payload=" + payload);
        final String[] headers = {
	    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:126.0) Gecko/20100101 Firefox/126.0",
	    "Accept: */*",
	    "Accept-Encoding: gzip, deflate",
	    "Connection: keep-alive",
	    "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3",
	    "Content-Type: application/x-www-form-urlencoded"
        };
	return sendRequest(url, requestMethod, payload, headers);
    }

    Response sendRequest(String url, String method, String payload, String[] headers) throws IOException
    {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpRequestBase request = createRequest(method, url, payload, headers);
	    return  executeRequest(httpClient, request);
        }
    }

    HttpRequestBase createRequest(String method, String url, String payload, String[] headers) throws IOException
    {
        final HttpRequestBase request;
        if ("GET".equalsIgnoreCase(method))
	{
            request = new HttpGet(url + "?" + payload);
        } else
	    if ("POST".equalsIgnoreCase(method))
	    {
            HttpPost postRequest = new HttpPost(url);
            postRequest.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
            request = postRequest;
        } else
            throw new IllegalArgumentException("Unsupported request method: " + method);
        for (String header : headers)
	{
            String[] headerParts = header.split(": ");
            request.addHeader(headerParts[0], headerParts[1]);
        }
        return request;
    }

    Response executeRequest(CloseableHttpClient httpClient, HttpRequestBase request) throws IOException
    {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
	    return gson.fromJson(handleResponse(response), Response.class);
        }
    }

    String handleResponse(CloseableHttpResponse response) throws IOException
    {
        List<String> links = new ArrayList<>();
        HttpEntity entity = response.getEntity();
        if (entity == null)
	    throw new NullPointerException("entity");
	String responseString = EntityUtils.toString(entity);
	if (response.getStatusLine().getStatusCode() != 200)
	    throw new RuntimeException("status code: " + response.getStatusLine().getStatusCode());
	if (isJson(responseString)) 
	    return responseString;
	log.error("Received non-JSON response or error code: " + response.getStatusLine());
	log.error("Response: " + responseString);
	return null;
    }

    boolean isJson(String responseString)
    {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(responseString);
            return true;
        } catch (IOException e)
	{
            return false;
        }
    }

    static public final class Response
    {
	public String query;
	public int number_of_results;
	public List<Item> results;
    }

    static public final class Item
    {
	public String url, title, content, engine, template, category, thumbnail;
	public List<String> parsed_url, engines;
	List<Integer> positions;
	float score;
    }
}
