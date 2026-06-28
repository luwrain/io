// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.osm;

import java.util.*;
import java.io.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.console.*;
import org.luwrain.popups.*;

import org.luwrain.app.base.*;

import org.luwrain.io.api.osm.nominatim.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase implements ConsoleArea.Appearance<NominatimPlace>
{
    private final App app;
    final List<NominatimPlace> elements = new ArrayList<>();
    final ConsoleArea<NominatimPlace> consoleArea;
    final SimpleArea detailsArea;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	consoleArea = new ConsoleArea<NominatimPlace>(consoleParams(p -> {
		    p.model = new ListModel<>(elements);
		    p.clickHandler = (area, index, el) -> onClick(el);
		    p.inputHandler = (area, text) -> onInput(text);
		    p.appearance = this;
		    p.name = s.searchAreaName();
		}));
	detailsArea = new SimpleArea(getControlContext(), s.detailsAreaName());
	setAreaLayout(AreaLayout.TOP_BOTTOM,
		      consoleArea, null,
		      detailsArea, null);
    }

    boolean onClick(NominatimPlace place)
    {
	final var lines = new ArrayList<String>();

	// 1. Display name — the full human-readable address
	final var displayName = place.getDisplayName();
	if (displayName != null && !displayName.isBlank())
	    lines.add(displayName);

	// 2. OSM type and ID
	final var osmType = place.getOsmType();
	final var osmId = place.getOsmId();
	if (osmType != null || osmId != 0)
	{
	    final var sb = new StringBuilder();
	    sb.append("OSM: ");
	    sb.append(osmTypeToHuman(osmType));
	    if (osmId != 0)
	    {
		sb.append(" #");
		sb.append(osmId);
	    }
	    lines.add(sb.toString());
	}

	// 3. Category and type
	final var category = place.getCategory();
	final var type = place.getType();
	if ((category != null && !category.isBlank()) || (type != null && !type.isBlank()))
	{
	    final var sb = new StringBuilder();
	    sb.append("Category: ");
	    sb.append(category != null ? category : "");
	    if (type != null && !type.isBlank())
	    {
		sb.append(" / ");
		sb.append(type);
	    }
	    lines.add(sb.toString());
	}

	// 4. Coordinates
	final var lat = place.getLat();
	final var lon = place.getLon();
	if (lat != null || lon != null)
	{
	    final var sb = new StringBuilder();
	    sb.append("Coordinates: ");
	    sb.append(lat != null ? lat : "?");
	    sb.append(", ");
	    sb.append(lon != null ? lon : "?");
	    lines.add(sb.toString());
	}

	// 5. Bounding box
	if (place.getBoundingbox() != null && place.getBoundingbox().size() >= 4)
	{
	    lines.add("Bounding box:");
	    lines.add("  Latitude:  " + place.getBoundingMinLat() + " \u2013 " + place.getBoundingMaxLat());
	    lines.add("  Longitude: " + place.getBoundingMinLon() + " \u2013 " + place.getBoundingMaxLon());
	}

	// 6. Address components
	final var address = place.getAddress();
	if (address != null && !address.isEmpty())
	{
	    lines.add("Address components:");
	    for (final var entry : address.entrySet())
	    {
		final var key = entry.getKey();
		final var value = entry.getValue();
		if (value != null && !value.isBlank())
		    lines.add("  " + addressKeyToHuman(key) + ": " + value);
	    }
	}

	// 7. Extra tags (only non-empty)
	final var extratags = place.getExtratags();
	if (extratags != null && !extratags.isEmpty())
	{
	    lines.add("Extra information:");
	    for (final var entry : extratags.entrySet())
	    {
		final var value = entry.getValue();
		if (value != null && !value.isBlank())
		    lines.add("  " + entry.getKey() + ": " + value);
	    }
	}

	// 8. Names in other languages
	final var namedetails = place.getNamedetails();
	if (namedetails != null && !namedetails.isEmpty())
	{
	    lines.add("Names in other languages:");
	    for (final var entry : namedetails.entrySet())
	    {
		final var value = entry.getValue();
		if (value != null && !value.isBlank())
		    lines.add("  " + entry.getKey() + ": " + value);
	    }
	}

	// 9. Importance
	final var importance = place.getImportance();
	if (importance > 0)
	    lines.add("Importance: " + importance);

	detailsArea.setLines(lines.toArray(new String[lines.size()]));
	detailsArea.setHotPoint(0, 0);
	setActiveArea(detailsArea);
	return true;
    }

    ConsoleArea.InputHandler.Result onInput(String text)
    {
	if (text.isEmpty())
	    return ConsoleArea.InputHandler.Result.REJECTED;
	final var taskId = app.newTaskId();
	if (app.runTask(taskId, () -> {
		    final List<NominatimPlace> results;
		    try
		    {
			results = app.nominatim.search(text);
		    }
		    catch (IOException e)
		    {
			app.finishedTask(taskId, () -> {
				app.setEventResponse(text(Sounds.DONE, app.getStrings().nothingFound()));
				consoleArea.refresh();
			    });
			return;
		    }
		    app.finishedTask(taskId, () -> {
			    elements.clear();
			    if (!results.isEmpty())
			    {
				elements.addAll(results);
				getLuwrain().playSound(Sounds.DONE);
			    } else
				app.setEventResponse(text(Sounds.DONE, app.getStrings().nothingFound()));
			    consoleArea.refresh();
			});
		}))
	    return ConsoleArea.InputHandler.Result.OK;
	return ConsoleArea.InputHandler.Result.REJECTED;
    }

    @Override public void announceItem(NominatimPlace place)
    {
	final var name = place.getDisplayName();
	if (name != null && !name.trim().isEmpty())
	    app.setEventResponse(listItem(name));
    }

    @Override public String getTextAppearance(NominatimPlace place)
    {
	return requireNonNullElse(place.getDisplayName(), "");
    }

    static private String osmTypeToHuman(String osmType)
    {
	if (osmType == null)
	    return "unknown";
	switch (osmType)
	{
	case "node":
	    return "Node";
	case "way":
	    return "Way";
	case "relation":
	    return "Relation";
	default:
	    return osmType;
	}
    }

    static private String addressKeyToHuman(String key)
    {
	switch (key)
	{
	case "house_number":
	    return "House number";
	case "road":
	    return "Road";
	case "pedestrian":
	    return "Pedestrian area";
	case "footway":
	    return "Footway";
	case "path":
	    return "Path";
	case "city":
	    return "City";
	case "town":
	    return "Town";
	case "village":
	    return "Village";
	case "hamlet":
	    return "Hamlet";
	case "suburb":
	    return "Suburb";
	case "district":
	    return "District";
	case "county":
	    return "County";
	case "state":
	    return "State";
	case "region":
	    return "Region";
	case "country":
	    return "Country";
	case "country_code":
	    return "Country code";
	case "postcode":
	    return "Postcode";
	case "building":
	    return "Building";
	case "amenity":
	    return "Amenity";
	case "shop":
	    return "Shop";
	case "tourism":
	    return "Tourism";
	case "historic":
	    return "Historic";
	case "leisure":
	    return "Leisure";
	case "office":
	    return "Office";
	case "public_transport":
	    return "Public transport";
	case "railway":
	    return "Railway";
	case "aeroway":
	    return "Aeroway";
	case "natural":
	    return "Natural";
	case "landuse":
	    return "Land use";
	case "waterway":
	    return "Waterway";
	case "place":
	    return "Place";
	default:
	    return key;
	}
    }
}
