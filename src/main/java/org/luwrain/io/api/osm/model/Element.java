package org.luwrain.io.api.osm.model;

import java.util.Map;
import lombok.*;

@Data
public abstract class Element
{
    private long id;
    private Map<String, String> tags;

    public Element(long id, Map<String, String> tags) {
        this.id = id;
        this.tags = tags;
    }

    /*
    public long getId() {
        return id;
    }

    public Map<String, String> getTags() {
        return tags;
    }
    */
}
