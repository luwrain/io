package org.luwrain.io.api.osm.model;

import java.util.Map;
import lombok.*;

@Data
public abstract class Element
{
static public final String
    NAME = "name";
    
    private long id;
    private Map<String, String> tags;

    public Element(long id, Map<String, String> tags)
    {
        this.id = id;
        this.tags = tags;
    }
}
