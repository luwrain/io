package org.luwrain.io.api.osm.nominatim;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominatimDetailsResult
{
    @SerializedName("place_id")
    private long placeId;

    @SerializedName("parent_place_id")
    private long parentPlaceId;

    @SerializedName("osm_type")
    private String osmType;

    @SerializedName("osm_id")
    private long osmId;

    @SerializedName("category")
    private String category;

    @SerializedName("type")
    private String type;

    @SerializedName("admin_level")
    private String adminLevel;

    @SerializedName("localname")
    private String localname;

    @SerializedName("names")
    private Map<String, String> names;

    @SerializedName("addresstags")
    private Map<String, String> addresstags;

    @SerializedName("housenumber")
    private String housenumber;

    @SerializedName("calculated_postcode")
    private String calculatedPostcode;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("indexed_date")
    private String indexedDate;

    @SerializedName("importance")
    private double importance;

    @SerializedName("calculated_importance")
    private double calculatedImportance;

    @SerializedName("extratags")
    private Map<String, String> extratags;

    @SerializedName("calculated_wikipedia")
    private String calculatedWikipedia;

    @SerializedName("rank_address")
    private int rankAddress;

    @SerializedName("rank_search")
    private int rankSearch;

    @SerializedName("isarea")
    private boolean isarea;

    @SerializedName("centroid")
    private NominatimPoint centroid;

    @SerializedName("geometry")
    private NominatimPoint geometry;

    @SerializedName("address")
    private List<NominatimDetailsResult> address;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NominatimPoint
    {
        private String type;
        private List<Double> coordinates;
    }
}
