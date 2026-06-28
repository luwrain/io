// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.osm.overpass;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OverpassClient
{
    static private final String DEFAULT_BASE_URL = "https://overpass-api.de";

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl;

    public OverpassClient()
    {
        this(DEFAULT_BASE_URL);
    }

    public OverpassClient(String baseUrl)
    {
        this.httpClient = new OkHttpClient();
        this.gson = new GsonBuilder()
            .registerTypeAdapter(OverpassElement.class, new OverpassElementDeserializer())
            .create();
        this.baseUrl = baseUrl;
    }

    /**
     * Executes a raw Overpass QL query and returns the parsed response.
     *
     * @param query the Overpass QL query string
     * @return the parsed response
     * @throws IOException on network error
     */
    public OverpassResponse query(String query) throws IOException
    {
        final String url = baseUrl + "/api/interpreter?data=" + encode(query);
        final String json = get(url);
        return gson.fromJson(json, OverpassResponse.class);
    }

    /**
     * Finds all streets that intersect (share a node with) the given street
     * in the specified city.
     *
     * <p>The algorithm:</p>
     * <ol>
     *   <li>Find the way(s) matching the street name and city</li>
     *   <li>Collect all their node IDs via <code>way(bn)</code></li>
     *   <li>Find other named highway ways that use any of those nodes</li>
     *   <li>Return the unique set of intersecting street names</li>
     * </ol>
     *
     * @param streetName the name of the street to search from
     * @param cityName   the city name for narrowing the search
     * @return list of intersecting street names, never null
     * @throws IOException on network error
     */
    public List<String> adjacentStreets(String streetName, String cityName) throws IOException
    {
        final String query = "[out:json];\n"
            + "way[\"highway\"][\"name\"=\"" + escape(streetName) + "\"][\"addr:city\"=\"" + escape(cityName) + "\"];\n"
            + "out body;\n"
            + "way(bn)[\"highway\"][\"name\"];\n"
            + "out tags;";
        final OverpassResponse resp = query(query);
        if (resp == null || resp.getElements() == null || resp.getElements().isEmpty())
            return List.of();

        final Set<String> names = new LinkedHashSet<>();
        for (final OverpassElement el : resp.getElements())
        {
            if (!"way".equals(el.getType()))
                continue;
            final String name = el.tag("name");
            if (name == null || name.isBlank())
                continue;
            if (name.equalsIgnoreCase(streetName))
                continue;
            names.add(name);
        }
        return new ArrayList<>(names);
    }

    /**
     * Finds all streets that intersect the given street, without requiring
     * a city filter. The search is performed on the ways found by name.
     *
     * @param streetName the name of the street to search from
     * @return list of intersecting street names, never null
     * @throws IOException on network error
     */
    public List<String> adjacentStreets(String streetName) throws IOException
    {
        final String query = "[out:json];\n"
            + "way[\"highway\"][\"name\"=\"" + escape(streetName) + "\"];\n"
            + "out body;\n"
            + "way(bn)[\"highway\"][\"name\"];\n"
            + "out tags;";
        final OverpassResponse resp = query(query);
        if (resp == null || resp.getElements() == null || resp.getElements().isEmpty())
            return List.of();

        final Set<String> names = new LinkedHashSet<>();
        for (final OverpassElement el : resp.getElements())
        {
            if (!"way".equals(el.getType()))
                continue;
            final String name = el.tag("name");
            if (name == null || name.isBlank())
                continue;
            if (name.equalsIgnoreCase(streetName))
                continue;
            names.add(name);
        }
        return new ArrayList<>(names);
    }

    /**
     * Finds the geometry (coordinates) of a street by name in a city.
     *
     * @param streetName the street name
     * @param cityName   the city name
     * @return list of ways representing the street, with geometry included
     * @throws IOException on network error
     */
    public List<OverpassWay> getStreetGeometry(String streetName, String cityName) throws IOException
    {
        final String query = "[out:json];\n"
            + "way[\"highway\"][\"name\"=\"" + escape(streetName) + "\"][\"addr:city\"=\"" + escape(cityName) + "\"];\n"
            + "out geom;";
        final OverpassResponse resp = query(query);
        if (resp == null || resp.getElements() == null)
            return List.of();
        final List<OverpassWay> ways = new ArrayList<>();
        for (final OverpassElement el : resp.getElements())
        {
            if (el instanceof OverpassWay)
                ways.add((OverpassWay)el);
        }
        return ways;
    }

    /**
     * Finds nodes by an address composed of city, street, and house number.
     *
     * @param city        city name
     * @param street      street name
     * @param housenumber house number
     * @return list of matching nodes
     * @throws IOException on network error
     */
    public List<OverpassNode> getNodesByAddress(String city, String street, String housenumber) throws IOException
    {
        final String query = "[out:json];\n"
            + "node[\"addr:city\"=\"" + escape(city) + "\"]"
            + "[\"addr:street\"=\"" + escape(street) + "\"]"
            + "[\"addr:housenumber\"=\"" + escape(housenumber) + "\"];\n"
            + "out body;";
        final OverpassResponse resp = query(query);
        return filterNodes(resp);
    }

    /**
     * Finds elements by name and type.
     *
     * @param type the OSM type: "node", "way", or "relation"
     * @param name the name to search (case-insensitive regex match)
     * @return list of matching elements
     * @throws IOException on network error
     */
    public List<OverpassElement> findByName(String type, String name) throws IOException
    {
        final String query = "[out:json];\n"
            + type + "[\"name\"~\"" + escape(name) + "\"];\n"
            + "out body;";
        final OverpassResponse resp = query(query);
        if (resp == null || resp.getElements() == null)
            return List.of();
        return resp.getElements();
    }

    /**
     * Finds the OSM element nearest to the given coordinates.
     *
     * @param type the OSM type to search for
     * @param lat  latitude
     * @param lon  longitude
     * @return the nearest element, or null if nothing found
     * @throws IOException on network error
     */
    public OverpassElement getByCoordinate(String type, double lat, double lon) throws IOException
    {
        final String query = "[out:json];\n"
            + type + "(around:1," + lat + "," + lon + ");\n"
            + "out body;";
        final OverpassResponse resp = query(query);
        if (resp == null || resp.getElements() == null || resp.getElements().isEmpty())
            return null;
        return resp.getElements().get(0);
    }

    /**
     * Gets a single element by type and ID.
     *
     * @param type the OSM type: "node", "way", or "relation"
     * @param id   the OSM ID
     * @return the element, or null if not found
     * @throws IOException on network error
     */
    public OverpassElement getById(String type, long id) throws IOException
    {
        final String query = "[out:json];\n"
            + type + "(" + id + ");\n"
            + "out body;";
        final OverpassResponse resp = query(query);
        if (resp == null || resp.getElements() == null || resp.getElements().isEmpty())
            return null;
        return resp.getElements().get(0);
    }

    /**
     * Finds all named highway ways in a city.
     *
     * @param cityName the city name
     * @return list of named ways
     * @throws IOException on network error
     */
    public List<OverpassWay> getStreetsInCity(String cityName) throws IOException
    {
        final String query = "[out:json];\n"
            + "area[\"name\"=\"" + escape(cityName) + "\"]->.city;\n"
            + "way(area.city)[\"highway\"][\"name\"];\n"
            + "out tags;";
        final OverpassResponse resp = query(query);
        if (resp == null || resp.getElements() == null)
            return List.of();
        final List<OverpassWay> ways = new ArrayList<>();
        for (final OverpassElement el : resp.getElements())
        {
            if (el instanceof OverpassWay)
                ways.add((OverpassWay)el);
        }
        return ways;
    }

    /**
     * Finds public transport stops (bus, tram, trolleybus) near ways on a
     * given street in a given city.
     *
     * @param city   the city name
     * @param street the street name
     * @param radius radius in meters around the street
     * @return list of stop nodes
     * @throws IOException on network error
     */
    public List<OverpassNode> publicTransportStopsOnStreet(String city, String street, int radius) throws IOException
    {
        final String query = "[out:json];\n"
            + "way[\"highway\"][\"name\"=\"" + escape(street) + "\"][\"addr:city\"=\"" + escape(city) + "\"];\n"
            + "node(around:" + radius + ")[\"highway\"=\"bus_stop\"];\n"
            + "out body;";
        final OverpassResponse resp = query(query);
        return filterNodes(resp);
    }

    /**
     * Finds institutions (amenity, shop, tourism, etc.) near a street.
     *
     * @param city   the city name (may be null to skip city filter)
     * @param street the street name
     * @param tags   the tags to search for (e.g. {"amenity": "university"})
     * @param radius radius in meters around the street
     * @return list of matching nodes
     * @throws IOException on network error
     */
    public List<OverpassNode> institutionsOnStreet(String city, String street,
                                                    Map<String, String> tags, int radius) throws IOException
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("[out:json];\n");
        sb.append("way[\"highway\"][\"name\"=\"").append(escape(street)).append("\"]");
        if (city != null && !city.isBlank())
            sb.append("[\"addr:city\"=\"").append(escape(city)).append("\"]");
        sb.append(";\n");
        sb.append("node(around:").append(radius).append(")");
        for (final var entry : tags.entrySet())
            sb.append("[\"").append(escape(entry.getKey())).append("\"=\"").append(escape(entry.getValue())).append("\"]");
        sb.append(";\n");
        sb.append("out body;");
        final OverpassResponse resp = query(sb.toString());
        return filterNodes(resp);
    }

    /**
     * Finds public transport route relations in a city.
     *
     * @param city the city name
     * @return list of route names (ref + name from tags)
     * @throws IOException on network error
     */
    public List<String> publicTransportRoutesInCity(String city) throws IOException
    {
        final String query = "[out:json];\n"
            + "area[\"name\"=\"" + escape(city) + "\"]->.cityArea;\n"
            + "relation(area.cityArea)[\"type\"=\"route\"][\"route\"~\"bus|tram|trolleybus\"];\n"
            + "out tags;";
        final OverpassResponse resp = query(query);
        if (resp == null || resp.getElements() == null)
            return List.of();
        final List<String> result = new ArrayList<>();
        for (final OverpassElement el : resp.getElements())
        {
            final String ref = el.tag("ref");
            final String name = el.tag("name");
            if (name != null && !name.isBlank())
                result.add(name);
            else if (ref != null && !ref.isBlank())
                result.add(ref);
        }
        return result;
    }

    /**
     * Finds public transport stops on a specific route in a city.
     *
     * @param city        the city name
     * @param routeNumber the route number (ref tag value)
     * @return list of stop nodes
     * @throws IOException on network error
     */
    public List<OverpassNode> publicTransportStopsOnRoute(String city, String routeNumber) throws IOException
    {
        final String query = "[out:json];\n"
            + "area[\"name\"=\"" + escape(city) + "\"]->.cityArea;\n"
            + "relation[\"ref\"=\"" + escape(routeNumber) + "\"](area.cityArea)->.route;\n"
            + "node(r.route)[\"highway\"=\"bus_stop\"];\n"
            + "out body;";
        final OverpassResponse resp = query(query);
        return filterNodes(resp);
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

    /**
     * Escapes special characters for use inside Overpass QL quoted strings.
     * Backslash and double-quote are escaped; control characters are removed.
     */
    static private String escape(String value)
    {
        if (value == null)
            return "";
        final StringBuilder sb = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++)
        {
            final char c = value.charAt(i);
            switch (c)
            {
            case '\\':
                sb.append("\\\\");
                break;
            case '"':
                sb.append("\\\"");
                break;
            case '\n':
            case '\r':
            case '\t':
                break;
            default:
                if (c < 0x20)
                    break;
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static private List<OverpassNode> filterNodes(OverpassResponse resp)
    {
        if (resp == null || resp.getElements() == null)
            return List.of();
        final List<OverpassNode> nodes = new ArrayList<>();
        for (final OverpassElement el : resp.getElements())
        {
            if (el instanceof OverpassNode)
                nodes.add((OverpassNode)el);
        }
        return nodes;
    }
}
