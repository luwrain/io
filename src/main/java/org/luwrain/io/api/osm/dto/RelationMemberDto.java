package dto;

import com.google.gson.annotations.SerializedName;

public class RelationMemberDto {
    @SerializedName("type")
    private String type;

    @SerializedName("ref")
    private long ref;

    @SerializedName("role")
    private String role;

    public String getType() {
        return type;
    }

    public long getRef() {
        return ref;
    }

    public String getRole() {
        return role;
    }
}
