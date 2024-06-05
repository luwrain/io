package dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RelationDto extends ElementDto {
    @SerializedName("members")
    private List<RelationMemberDto> members;

    public List<RelationMemberDto> getMembers() {
        return members;
    }
}
