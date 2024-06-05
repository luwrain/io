package org.luwrain.io.api.osm.model;

import java.util.Map;

public class Node extends Element{
    private double lat;
    private double lon;

    public Node(long id, Map<String, String> tags, double lat, double lon) {
        super(id, tags);
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
