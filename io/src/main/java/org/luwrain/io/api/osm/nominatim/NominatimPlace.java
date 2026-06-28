// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.osm.nominatim;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominatimPlace
{
    @SerializedName("place_id")
    private long placeId;

    @SerializedName("licence")
    private String licence;

    @SerializedName("osm_type")
    private String osmType;

    @SerializedName("osm_id")
    private long osmId;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("category")
    private String category;

    @SerializedName("type")
    private String type;

    @SerializedName("importance")
    private double importance;

    @SerializedName("boundingbox")
    private List<String> boundingbox;

    @SerializedName("address")
    private Map<String, String> address;

    @SerializedName("extratags")
    private Map<String, String> extratags;

    @SerializedName("namedetails")
    private Map<String, String> namedetails;

    public double getLatAsDouble()
    {
        return lat != null ? Double.parseDouble(lat) : 0.0;
    }

    public double getLonAsDouble()
    {
        return lon != null ? Double.parseDouble(lon) : 0.0;
    }

    public double getBoundingMinLat()
    {
        if (boundingbox == null || boundingbox.size() < 1)
            return 0.0;
        return Double.parseDouble(boundingbox.get(0));
    }

    public double getBoundingMaxLat()
    {
        if (boundingbox == null || boundingbox.size() < 2)
            return 0.0;
        return Double.parseDouble(boundingbox.get(1));
    }

    public double getBoundingMinLon()
    {
        if (boundingbox == null || boundingbox.size() < 3)
            return 0.0;
        return Double.parseDouble(boundingbox.get(2));
    }

    public double getBoundingMaxLon()
    {
        if (boundingbox == null || boundingbox.size() < 4)
            return 0.0;
        return Double.parseDouble(boundingbox.get(3));
    }
}
