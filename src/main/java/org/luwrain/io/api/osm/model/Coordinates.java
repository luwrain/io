package model;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    String Type;

    String Id;

    String displayName;

    double lat;
    double lon;

    double [] boundingBox;

    public Coordinates(String type, String id, String displayName, double lat, double lon, double[] boundingBox) {
        this.Type = type;
        this.Id = id;
        this.displayName = displayName;
        this.lat = lat;
        this.lon = lon;
        this.boundingBox = boundingBox;
    }

    public String getType() {return Type;}
    public String getId() {return Id;}
    public String getDisplayName() {return displayName;}
    public double getLat() {return lat;}
    public double getLon() {return lon;}
    public double[] getBoundingBox() {return boundingBox;}
}
