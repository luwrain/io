package org.luwrain.io.api.osm.model;

public class Address {
    String Type;

    String Id;

    String displayName;

    public Address(String type, String id, String displayName) {
        this.Type = type;
        this.Id = id;
        this.displayName = displayName;
    }

    public String getType() {return Type;}
    public String getId() {return Id;}
    public String getDisplayName() {return displayName;}
}
