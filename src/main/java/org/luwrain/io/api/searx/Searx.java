package org.luwrain.io.api.searx;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Searx {

    private static final Logger logger = LoggerFactory.getLogger(Searx.class);
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        String query = "coffee";
        String requestMethod = "POST";
        String[] searxngUrls = {
                "http://localhost:8080/search", // локальный searxng
                "https://search.mdosch.de/search",
                "https://search.projectsegfau.lt/search",
                "https://search.us.projectsegfau.lt/search",
                "https://searx.sev.monster/search",
                "https://etsi.me/search",
                "https://search.in.projectsegfau.lt/search"
        };
        String searxngUrl = searxngUrls[0];
        String payload = "q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&format=json";

        String[] headers = {
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:126.0) Gecko/20100101 Firefox/126.0",
                "Accept: */*",
                "Accept-Encoding: gzip, deflate",
                "Connection: keep-alive",
                "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3",
                "Content-Type: application/x-www-form-urlencoded"
        };

        List<String> links = sendRequest(searxngUrl, requestMethod, payload, headers);
        String json = gson.toJson(links);
        printLinksAsJson(json);
    }

    private static List<String> sendRequest(String url, String method, String payload, String[] headers) {
        List<String> links = new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpRequestBase request = createRequest(method, url, payload, headers);
            links = executeRequest(httpClient, request);
        } catch (IOException e) {
            logger.error("Error occurred while making HTTP request", e);
        }
        return links;
    }

    private static HttpRequestBase createRequest(String method, String url, String payload, String[] headers) throws IOException {
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

    private static List<String> executeRequest(CloseableHttpClient httpClient, HttpRequestBase request) throws IOException {
        List<String> links = new ArrayList<>();
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            links = handleResponse(response);
        }
        return links;
    }

    private static List<String> handleResponse(CloseableHttpResponse response) throws IOException {
        List<String> links = new ArrayList<>();
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            if (response.getStatusLine().getStatusCode() == 200 && isJson(responseString)) {
                links = parseAndPrintLinks(responseString);
            } else {
                logger.error("Received non-JSON response or error code: " + response.getStatusLine());
                logger.error("Response: " + responseString);
            }
        }
        return links;
    }

    private static boolean isJson(String responseString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(responseString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static List<String> parseAndPrintLinks(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultsNode = rootNode.path("results");

        List<String> links = new ArrayList<>();
        for (JsonNode result : resultsNode) {
            String url = result.path("url").asText();
            links.add(url);
        }
        return links;
    }

    private static void printLinksAsJson(String json) {
        logger.info(json);
    }
}
