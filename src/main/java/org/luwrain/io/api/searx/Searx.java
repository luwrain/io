package org.luwrain.io.api.searx;

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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Searx
{
    static public final String[] searxngUrls = {
	"http://localhost:8080/search", // локальный searxng
	"https://search.mdosch.de/search",
	"https://search.projectsegfau.lt/search",
	"https://search.us.projectsegfau.lt/search",
	"https://searx.sev.monster/search",
	"https://etsi.me/search",
	"https://search.in.projectsegfau.lt/search"
    };

    private static final Logger log = LogManager.getLogger();
    private static final Gson gson = new Gson();

    Response request(String query)
    {
        final String
	requestMethod = "POST",
	searxngUrl = searxngUrls[1],
	payload = "q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&format=json";
        final String[] headers = {
	    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:126.0) Gecko/20100101 Firefox/126.0",
	    "Accept: */*",
	    "Accept-Encoding: gzip, deflate",
	    "Connection: keep-alive",
	    "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3",
	    "Content-Type: application/x-www-form-urlencoded"
        };
	return sendRequest(searxngUrl, requestMethod, payload, headers);
    }

    Response sendRequest(String url, String method, String payload, String[] headers)
    {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpRequestBase request = createRequest(method, url, payload, headers);
	    return  executeRequest(httpClient, request);
        }
	catch (IOException e)
	{
            log.catching(e);
	    throw new RuntimeException(e);
        }
    }

    HttpRequestBase createRequest(String method, String url, String payload, String[] headers) throws IOException
    {
        HttpRequestBase request;
        if ("GET".equalsIgnoreCase(method)) {
            request = new HttpGet(url + "?" + payload);
        } else if ("POST".equalsIgnoreCase(method)) {
            HttpPost postRequest = new HttpPost(url);
            postRequest.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
            request = postRequest;
        } else {
            throw new UnsupportedOperationException("Unsupported request method: " + method);
        }
        for (String header : headers) {
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
