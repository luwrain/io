package dto;

import com.google.gson.annotations.SerializedName;

public class AddressDto {
    @SerializedName("osm_type")
    String Type;

    @SerializedName("osm_id")
    String Id;

    @SerializedName("display_name")
    String displayName;

    public String getType() {return Type;}
    public String getId() {return Id;}
    public String getDisplayName() {return displayName;}
}

