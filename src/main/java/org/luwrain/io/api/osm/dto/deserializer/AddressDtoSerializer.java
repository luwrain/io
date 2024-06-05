package dto.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dto.AddressDto;

import java.lang.reflect.Type;

public class AddressDtoSerializer implements JsonDeserializer<AddressDto> {
    @Override
    public AddressDto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, AddressDto.class);
    }
}
