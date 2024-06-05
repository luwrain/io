package dto;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class ElementDto {
    @SerializedName("id")
    private long id;

    @SerializedName("tags")
    private Map<String, String> tags;

    public long getId() {
        return id;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
