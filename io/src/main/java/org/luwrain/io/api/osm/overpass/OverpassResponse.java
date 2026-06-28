// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>


package org.luwrain.io.api.osm.overpass;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverpassResponse
{
    @SerializedName("version")
    private double version;

    @SerializedName("generator")
    private String generator;

    @SerializedName("elements")
    private List<OverpassElement> elements;
}
