package dto;

import com.google.gson.annotations.SerializedName;

public class CoordinatesDto {
    @SerializedName("osm_type")
    String Type;

    @SerializedName("osm_id")
    String Id;

    @SerializedName("display_name")
    String displayName;

    @SerializedName("lat")
    double lat;

    @SerializedName("lon")
    double lon;

    @SerializedName("boundingbox")
    double [] boundingBox;

    public String getType() {return Type;}
    public String getId() {return Id;}
    public String getDisplayName() {return displayName;}
    public double getLat() {return lat;}
    public double getLon() {return lon;}
    public double[] getBoundingBox() {return boundingBox;}
}
