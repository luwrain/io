// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.osm.nominatim;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NominatimClient
{
    static private final String DEFAULT_BASE_URL = "https://nominatim.openstreetmap.org";

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl;

    public NominatimClient()
    {
        this(DEFAULT_BASE_URL);
    }

    public NominatimClient(String baseUrl)
    {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
        this.baseUrl = baseUrl;
    }

    /**
     * Free-form search: finds OSM objects by a textual address query.
     *
     * @param query The search query, e.g. "Tomsk, Lenin avenue 36"
     * @return list of matching places, or empty list if nothing found
     * @throws IOException on network error
     */
    public List<NominatimPlace> search(String query) throws IOException
    {
        final String url = baseUrl + "/search?q=" + encode(query) + "&format=json&addressdetails=1";
        final String json = get(url);
        final Type listType = new TypeToken<List<NominatimPlace>>() {}.getType();
        final List<NominatimPlace> result = gson.fromJson(json, listType);
        return result != null ? result : List.of();
    }

    /**
     * Structured search: finds OSM objects using individual address components.
     * Parameters that are null or blank are omitted.
     *
     * @param street     street name
     * @param city       city name
     * @param county     county name
     * @param state      state/province name
     * @param country    country name
     * @param postalcode postal code
     * @return list of matching places, or empty list if nothing found
     * @throws IOException on network error
     */
    public List<NominatimPlace> searchStructured(String street, String city,
                                                  String county, String state,
                                                  String country, String postalcode) throws IOException
    {
        final StringBuilder sb = new StringBuilder(baseUrl);
        sb.append("/search?format=json&addressdetails=1");
        appendParam(sb, "street", street);
        appendParam(sb, "city", city);
        appendParam(sb, "county", county);
        appendParam(sb, "state", state);
        appendParam(sb, "country", country);
        appendParam(sb, "postalcode", postalcode);
        final String json = get(sb.toString());
        final Type listType = new TypeToken<List<NominatimPlace>>() {}.getType();
        final List<NominatimPlace> result = gson.fromJson(json, listType);
        return result != null ? result : List.of();
    }

    /**
     * Search with full parameter control. All supported Nominatim search
     * parameters can be passed via the {@link SearchParams} builder.
     *
     * @param params the parameters to use
     * @return list of matching places, or empty list if nothing found
     * @throws IOException on network error
     */
    public List<NominatimPlace> search(SearchParams params) throws IOException
    {
        final String url = params.buildUrl(baseUrl);
        final String json = get(url);
        final Type listType = new TypeToken<List<NominatimPlace>>() {}.getType();
        final List<NominatimPlace> result = gson.fromJson(json, listType);
        return result != null ? result : List.of();
    }

    /**
     * Reverse geocoding: finds an address by latitude and longitude.
     *
     * @param lat latitude
     * @param lon longitude
     * @return the nearest place, or null if nothing found
     * @throws IOException on network error
     */
    public NominatimPlace reverse(double lat, double lon) throws IOException
    {
        final String url = baseUrl + "/reverse?format=json&lat=" + lat + "&lon=" + lon + "&addressdetails=1";
        final String json = get(url);
        final NominatimPlace result = gson.fromJson(json, NominatimPlace.class);
        if (result != null && result.getPlaceId() == 0 && result.getOsmId() == 0)
            return null;
        return result;
    }

    /**
     * Reverse geocoding with language preference.
     *
     * @param lat latitude
     * @param lon longitude
     * @param lang the preferred language, e.g. "ru" or "en"
     * @return the nearest place, or null if nothing found
     * @throws IOException on network error
     */
    public NominatimPlace reverse(double lat, double lon, String lang) throws IOException
    {
        final String url = baseUrl + "/reverse?format=json&lat=" + lat + "&lon=" + lon
            + "&addressdetails=1&accept-language=" + encode(lang);
        final String json = get(url);
        final NominatimPlace result = gson.fromJson(json, NominatimPlace.class);
        if (result != null && result.getPlaceId() == 0 && result.getOsmId() == 0)
            return null;
        return result;
    }

    /**
     * Lookup: retrieves OSM objects by their IDs.
     * The format is: "N123", "W456", "R789" (Node, Way, Relation).
     *
     * @param osmIds list of OSM identifiers, e.g. "N123456"
     * @return list of matching places
     * @throws IOException on network error
     */
    public List<NominatimPlace> lookup(List<String> osmIds) throws IOException
    {
        final String joined = String.join(",", osmIds);
        final String url = baseUrl + "/lookup?osm_ids=" + encode(joined)
            + "&format=json&addressdetails=1&extratags=1&namedetails=1";
        final String json = get(url);
        final Type listType = new TypeToken<List<NominatimPlace>>() {}.getType();
        final List<NominatimPlace> result = gson.fromJson(json, listType);
        return result != null ? result : List.of();
    }

    /**
     * Lookup a single OSM object by type and ID.
     *
     * @param osmType "N", "W", or "R"
     * @param osmId   the numeric OSM identifier
     * @return the place, or null if not found
     * @throws IOException on network error
     */
    public NominatimPlace lookup(String osmType, long osmId) throws IOException
    {
        final List<NominatimPlace> places = lookup(List.of(osmType + osmId));
        return places.isEmpty() ? null : places.get(0);
    }

    /**
     * Details: retrieves detailed information and address hierarchy for
     * a single OSM object.
     *
     * @param osmType "N", "W", or "R"
     * @param osmId   the numeric OSM identifier
     * @return the details result, or null if not found
     * @throws IOException on network error
     */
    public NominatimDetailsResult details(String osmType, long osmId) throws IOException
    {
        final String url = baseUrl + "/details?osmtype=" + encode(osmType)
            + "&osmid=" + osmId + "&format=json&addressdetails=1&extratags=1&namedetails=1";
        final String json = get(url);
        return gson.fromJson(json, NominatimDetailsResult.class);
    }

    /**
     * Status: checks the availability and metadata of the Nominatim service.
     *
     * @return the status result
     * @throws IOException on network error
     */
    public NominatimStatusResult status() throws IOException
    {
        final String url = baseUrl + "/status?format=json";
        final String json = get(url);
        return gson.fromJson(json, NominatimStatusResult.class);
    }

    private String get(String url) throws IOException
    {
        final Request request = new Request.Builder()
        .url(url)
        .header("User-Agent", "LUWRAIN-IO/1.0")
        .build();
        try (Response response = httpClient.newCall(request).execute())
        {
            if (!response.isSuccessful())
                throw new IOException("Unexpected response code " + response.code() + " for " + url);
            final var body = response.body();
            if (body == null)
                throw new IOException("Empty response body for " + url);
            return body.string();
        }
    }

    static private String encode(String value)
    {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    static private void appendParam(StringBuilder sb, String name, String value)
    {
        if (value != null && !value.isBlank())
        {
            sb.append('&');
            sb.append(name);
            sb.append('=');
            sb.append(encode(value));
        }
    }

    /**
     * Builder for Nominatim search parameters.
     * Provides type-safe construction of advanced search queries.
     */
    static public class SearchParams
    {
        private String q;
        private String street;
        private String city;
        private String county;
        private String state;
        private String country;
        private String postalcode;
        private String viewbox;
        private Boolean bounded;
        private Integer limit;
        private String acceptLanguage;
        private Boolean polygonGeojson;
        private Boolean extratags;
        private Boolean namedetails;

        public SearchParams() {}

        /** Free-form query string. */
        public SearchParams q(String val) { this.q = val; return this; }
        /** Street name (structured query). */
        public SearchParams street(String val) { this.street = val; return this; }
        /** City name (structured query). */
        public SearchParams city(String val) { this.city = val; return this; }
        /** County name (structured query). */
        public SearchParams county(String val) { this.county = val; return this; }
        /** State/province name (structured query). */
        public SearchParams state(String val) { this.state = val; return this; }
        /** Country name (structured query). */
        public SearchParams country(String val) { this.country = val; return this; }
        /** Postal code (structured query). */
        public SearchParams postalcode(String val) { this.postalcode = val; return this; }
        /** Bounding box filter: "minLon,minLat,maxLon,maxLat". */
        public SearchParams viewbox(String val) { this.viewbox = val; return this; }
        /** If true, restrict results to the viewbox strictly. */
        public SearchParams bounded(boolean val) { this.bounded = val; return this; }
        /** Maximum number of results to return. */
        public SearchParams limit(int val) { this.limit = val; return this; }
        /** Preferred language for results, e.g. "ru". */
        public SearchParams acceptLanguage(String val) { this.acceptLanguage = val; return this; }
        /** If true, include GeoJSON polygon in results. */
        public SearchParams polygonGeojson(boolean val) { this.polygonGeojson = val; return this; }
        /** If true, include extra OSM tags. */
        public SearchParams extratags(boolean val) { this.extratags = val; return this; }
        /** If true, include name details in various languages. */
        public SearchParams namedetails(boolean val) { this.namedetails = val; return this; }

        String buildUrl(String baseUrl)
        {
            final StringBuilder sb = new StringBuilder(baseUrl);
            sb.append("/search?format=json&addressdetails=1");
            appendParam(sb, "q", q);
            appendParam(sb, "street", street);
            appendParam(sb, "city", city);
            appendParam(sb, "county", county);
            appendParam(sb, "state", state);
            appendParam(sb, "country", country);
            appendParam(sb, "postalcode", postalcode);
            appendParam(sb, "viewbox", viewbox);
            if (bounded != null && bounded)
                sb.append("&bounded=1");
            if (limit != null)
                sb.append("&limit=").append(limit);
            appendParam(sb, "accept-language", acceptLanguage);
            if (polygonGeojson != null && polygonGeojson)
                sb.append("&polygon_geojson=1");
            if (extratags != null && extratags)
                sb.append("&extratags=1");
            if (namedetails != null && namedetails)
                sb.append("&namedetails=1");
            return sb.toString();
        }
    }
}
