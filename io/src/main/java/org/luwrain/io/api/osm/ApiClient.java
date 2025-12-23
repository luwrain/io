package org.luwrain.io.api.osm;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class ApiClient
{
    //    private static final String OVERPASS_API_URL = "http://overpass-api.de/api/interpreter";
    static private final String OVERPASS_API_URL = "https://maps.mail.ru/osm/tools/overpass/api/interpreter";
    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org";
    private OkHttpClient client;

    public ApiClient() {
        this.client = new OkHttpClient();
    }

    public String sendQuery(String query) throws IOException {
        Request request = new Request.Builder()
                .url(OVERPASS_API_URL + "?data=" + query)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }


    public String sendQueryToNominatim(String query) throws IOException
    {
        Request request = new Request.Builder()
                .url(NOMINATIM_API_URL + query)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
