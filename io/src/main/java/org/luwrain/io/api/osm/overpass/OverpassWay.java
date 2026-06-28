// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.osm.overpass;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OverpassWay extends OverpassElement
{
    @SerializedName("nodes")
    private List<Long> nodes;

    @SerializedName("geometry")
    private List<OverpassGeometryPoint> geometry;

    /**
     * Returns the street name from tags ("name" key), or null if absent.
     */
    public String streetName()
    {
        return name();
    }
}
