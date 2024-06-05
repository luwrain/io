package org.luwrain.io.api.yandex_disk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import ru.vorotov.yandex_disk_api_client_lib.exceptions.FileIsTooBigException;
import ru.vorotov.yandex_disk_api_client_lib.exceptions.TooManyRequestsException;
import ru.vorotov.yandex_disk_api_client_lib.exceptions.UnauthorizedException;
import ru.vorotov.yandex_disk_api_client_lib.exceptions.UnavailableException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class YandexDiskClient implements DiskClient {
    private static volatile YandexDiskClient INSTANCE;
    private String token = null;

    private final String url = "https://cloud-api.yandex.net/v1/disk";
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final Gson gson = new Gson();

    private YandexDiskClient() {
    }

    public static YandexDiskClient getInstance() {
        if (INSTANCE == null) {
            synchronized (YandexDiskClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new YandexDiskClient();
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public String upload(InputStream in, String filepath) throws IOException, UnauthorizedException, FileIsTooBigException, TooManyRequestsException, UnavailableException {
        String linkUpload = null;

        try {
            linkUpload = getLinkUpload(filepath);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException();
        } catch (FileIsTooBigException e) {
            throw new FileIsTooBigException();
        } catch (TooManyRequestsException e) {
            throw new TooManyRequestsException();
        } catch (UnavailableException e) {
            throw new UnavailableException();
        }

        try {
            URL urlForUpload = new URL(linkUpload);

            HttpURLConnection connection = (HttpURLConnection) urlForUpload.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();

            if (responseCode != 201 && responseCode != 202) {
                throw new RuntimeException("ERROR");
            }

            connection.disconnect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        makeFilePublic(filepath);
        return getPublicLink(filepath);
    }

    @Override
    public void authorize(String token) {
        this.token = token;
    }

    @Override
    public void cancelUpload(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelDownload(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream download(String link) throws FileNotFoundException, IOException, FileIsTooBigException, TooManyRequestsException, UnavailableException {
        try {
            link = getLinkDownload(link);
            URL downloadUrl = new URL(link);
            return downloadUrl.openStream();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Resource not found");
        } catch (IOException e) {
            throw new IOException(e);
        } catch (FileIsTooBigException e) {
            throw new FileIsTooBigException();
        } catch (TooManyRequestsException e) {
            throw new TooManyRequestsException();
        } catch (UnavailableException e) {
            throw new UnavailableException();
        }
    }

    private void makeFilePublic(String filepath) {
        String url = this.url + "/resources/publish" + "?path=/" + filepath;
        sendPutRequest(url);
    }

    private String getLinkUpload(String filepath) throws FileNotFoundException, IOException, UnauthorizedException, FileIsTooBigException, TooManyRequestsException, UnavailableException {
        String url = this.url + "/resources/upload" + "?path=/" + filepath;

        JsonObject jsonObject = sendGetRequest(url);

        switch (jsonObject.get("statusCode").getAsInt()) {
            case 401:
                throw new UnauthorizedException();
            case 404:
                throw new FileNotFoundException("Resource not found");
            case 413:
                throw new FileIsTooBigException();
            case 429:
                throw new TooManyRequestsException();
            case 503:
                throw new UnavailableException();
        }

        return jsonObject.get("href").getAsString();
    }

    private String getPublicLink(String filepath) {
        String url = this.url + "/resources" + "?path=/" + filepath;

        JsonObject jsonObject = sendGetRequest(url);

        return jsonObject.get("public_url").getAsString();
    }

    private String getLinkDownload(String link) throws FileNotFoundException, IOException, FileIsTooBigException, TooManyRequestsException, UnavailableException {
        String url = this.url + "/public/resources/download" + "?public_key=" + link;

        JsonObject jsonObject = sendGetRequest(url);

        switch (jsonObject.get("statusCode").getAsInt()) {
            case 404:
                throw new FileNotFoundException("Resource not found");
            case 413:
                throw new FileIsTooBigException();
            case 429:
                throw new TooManyRequestsException();
            case 503:
                throw new UnavailableException();
        }

        return jsonObject.get("href").getAsString();
    }

    public String getFileName(String link) {
        String url = this.url + "/public/resources" + "?public_key=" + link;

        JsonObject jsonObject = sendGetRequest(url);

        return jsonObject.get("name").getAsString();
    }

    public int getFileSize(String link) {
        String url = this.url + "/public/resources" + "?public_key=" + link;

        JsonObject jsonObject = sendGetRequest(url);

        return jsonObject.get("size").getAsInt();
    }

    private JsonObject sendPutRequest(String url) {
        HttpPut request = new HttpPut(url);
        return sendRequest(request);
    }

    private JsonObject sendGetRequest(String url) {
        HttpGet request = new HttpGet(url);
        return sendRequest(request);
    }

    private JsonObject sendRequest(HttpUriRequest request) {
        request.setHeader(HttpHeaders.AUTHORIZATION, this.token);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());

            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            jsonResponse.addProperty("statusCode", response.getCode());
            return jsonResponse;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}