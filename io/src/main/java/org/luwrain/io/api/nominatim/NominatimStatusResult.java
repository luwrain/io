package org.luwrain.io.api.nominatim;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominatimStatusResult
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data_updated")
    private String dataUpdated;

    @SerializedName("software_version")
    private String softwareVersion;

    @SerializedName("database_version")
    private String databaseVersion;
}
