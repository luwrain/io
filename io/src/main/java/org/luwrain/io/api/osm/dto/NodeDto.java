package org.luwrain.io.api.osm.dto;

import com.google.gson.annotations.SerializedName;

public class NodeDto extends ElementDto{
    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
