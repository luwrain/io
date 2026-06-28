// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.osm.overpass;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverpassElement
{
    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private long id;

    @SerializedName("tags")
    private Map<String, String> tags;

    /**
     * Returns the value of the "name" tag, or null if absent.
     */
    public String name()
    {
        return tags != null ? tags.get("name") : null;
    }

    /**
     * Returns the value of a specific tag, or null if absent.
     */
    public String tag(String key)
    {
        return tags != null ? tags.get(key) : null;
    }
}
