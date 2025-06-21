package org.luwrain.io.api.osm.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WayDto extends ElementDto{
    @SerializedName("nodes")
    private List<Long> nodes;

    public List<Long> getNodes() {
        return nodes;
    }
}
