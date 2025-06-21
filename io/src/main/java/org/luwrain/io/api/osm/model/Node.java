package org.luwrain.io.api.osm.model;

import java.util.Map;
import lombok.*;

@Data
public final class Node extends Element
{
    private final double lat;
    private final double lon;

    public Node(long id, Map<String, String> tags, double lat, double lon)
    {
        super(id, tags);
        this.lat = lat;
        this.lon = lon;
    }
    /*

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
    */
}
