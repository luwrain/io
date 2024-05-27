package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearxngModule {

    private static final Logger logger = LoggerFactory.getLogger(SearxngModule.class);

    public static void main(String[] args) {
        String query = "coffee"; // поисковой запрос
        String requestMethod = "POST"; // метод
        String searxngUrl = "https://searx.sev.monster/search"; // URL searxng клиента
        String payload = "q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&format=json";

        // заголовки которые я добавил чтобы копия из интернета работала
        String[] headers = {
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:126.0) Gecko/20100101 Firefox/126.0",
                "Accept: */*",
                "Accept-Encoding: gzip, deflate",
                "Connection: keep-alive",
                "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3",
                "Content-Type: application/x-www-form-urlencoded"
        };

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpRequestBase request = createRequest(requestMethod, searxngUrl, payload, headers);
            executeRequest(httpClient, request);
        } catch (IOException e) {
            logger.error("Error occurred while making HTTP request", e);
        }
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

    private static void executeRequest(CloseableHttpClient httpClient, HttpRequestBase request) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            handleResponse(response);
        }
    }

    private static void handleResponse(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            if (response.getStatusLine().getStatusCode() == 200 && isJson(responseString)) {
                List<String> links = parseAndPrintLinks(responseString);
                printLinks(links);
            } else {
                logger.error("Received non-JSON response or error code: " + response.getStatusLine());
                logger.error("Response: " + responseString);
            }
        }
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

    private static void printLinks(List<String> links) {
        for (String link : links) {
            logger.info(link);
        }
    }
}
