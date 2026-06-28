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
public class OverpassRelation extends OverpassElement
{
    @SerializedName("members")
    private List<OverpassMember> members;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverpassMember
    {
        @SerializedName("type")
        private String type;

        @SerializedName("ref")
        private long ref;

        @SerializedName("role")
        private String role;
    }
}
