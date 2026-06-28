// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.osm.overpass;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class OverpassElementDeserializer implements JsonDeserializer<OverpassElement>
{
    @Override public OverpassElement deserialize(JsonElement json, Type typeOfT,
                                                  JsonDeserializationContext context)
        throws JsonParseException
    {
        final JsonObject obj = json.getAsJsonObject();
        final JsonElement typeEl = obj.get("type");
        if (typeEl == null || !typeEl.isJsonPrimitive())
            throw new JsonParseException("Missing or invalid 'type' field in Overpass element");
        final String type = typeEl.getAsString();
        switch (type)
        {
        case "node":
            return context.deserialize(json, OverpassNode.class);
        case "way":
            return context.deserialize(json, OverpassWay.class);
        case "relation":
            return context.deserialize(json, OverpassRelation.class);
        default:
            throw new JsonParseException("Unknown Overpass element type: " + type);
        }
    }
}
