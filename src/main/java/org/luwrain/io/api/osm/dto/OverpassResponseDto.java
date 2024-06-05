package dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OverpassResponseDto {
    @SerializedName("elements")
    private List<ElementDto> elements;

    public List<ElementDto> getElements() {
        return elements;
    }
}
