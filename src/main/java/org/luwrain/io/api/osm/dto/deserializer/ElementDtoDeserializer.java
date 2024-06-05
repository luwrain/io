package org.luwrain.io.api.osm.dto.deserializer;

import com.google.gson.*;
import java.lang.reflect.Type;
import org.luwrain.io.api.osm.dto.*;

public class ElementDtoDeserializer implements JsonDeserializer<ElementDto> {
    @Override
    public ElementDto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String elementType = jsonObject.get("type").getAsString();

        switch (elementType) {
            case "node":
                return context.deserialize(json, NodeDto.class);
            case "way":
                return context.deserialize(json, WayDto.class);
            case "relation":
                return context.deserialize(json, RelationDto.class);
            default:
                throw new JsonParseException("Unknown element type: " + elementType);
        }
    }
}
