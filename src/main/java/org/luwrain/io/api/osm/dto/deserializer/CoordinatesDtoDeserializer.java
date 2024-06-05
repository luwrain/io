package dto.deserializer;

import com.google.gson.*;
import java.lang.reflect.Type;
import dto.*;

public class CoordinatesDtoDeserializer implements JsonDeserializer<CoordinatesDto> {
    @Override
    public CoordinatesDto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, CoordinatesDto.class);


    }

}
