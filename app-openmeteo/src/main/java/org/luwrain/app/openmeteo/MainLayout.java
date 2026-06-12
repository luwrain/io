// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.openmeteo;

import java.util.*;
import java.io.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.console.*;
import org.luwrain.app.openmeteo.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleArea.*;

final class MainLayout extends LayoutBase  implements
					       ClickHandler<Entry>,
					       Appearance<Entry>,
					       InputHandler
{
    static private final Logger log = LogManager.getLogger();

    final List<Entry> entries = new ArrayList<>();
    final ConsoleArea<Entry> area;
    final SimpleArea messageArea;
    private final App app;
    private final WeatherQuery weatherQuery;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.weatherQuery = new WeatherQuery(app);
	this.messageArea = new SimpleArea(getControlContext(), app.getStrings().appName());
	this.area = new ConsoleArea<Entry>(consoleParams(p ->{
		    p.name = app.getStrings().appName();
		    p.model = new ListModel<Entry>(entries);
		    p.appearance = this;
		    p.inputHandler = this;
		    p.clickHandler = this;
		    p.inputPos = ConsoleArea.InputPos.TOP;
		    p.inputPrefix = app.getStrings().inputPrefix();
		}));
	setPropertiesHandler(area, a -> new OptionsLayout(app, getReturnAction()));
	setAreaLayout(area, null);
    }

    @Override public InputHandler.Result onConsoleInput(ConsoleArea area, String text)
    {
	final var query = text.trim();
	if (query.isEmpty())
	    return InputHandler.Result.OK;
	entries.add(new Entry(Entry.Type.QUERY, query, null));
	area.refresh();

	// Определяем, ввели координаты или название города
	String lat, lon;
	if (query.contains(","))
	{
	    // Формат: "широта,долгота"
	    final var parts = query.split(",", 2);
	    lat = parts[0].trim();
	    lon = parts[1].trim();
	}
	else
	{
	    // Геокодирование названия города
	    final var coords = weatherQuery.geocode(query);
	    if (coords == null)
	    {
		entries.add(new Entry(Entry.Type.ERROR, app.getStrings().geocodingError(), query));
		area.refresh();
		return InputHandler.Result.CLEAR_INPUT;
	    }
	    lat = coords[0];
	    lon = coords[1];
	}

	final var locationName = lat + ", " + lon;
	final var weatherText = weatherQuery.getWeather(lat, lon);
	if (weatherText == null)
	{
	    entries.add(new Entry(Entry.Type.ERROR, app.getStrings().forecastError(), locationName));
	    area.refresh();
	    return InputHandler.Result.CLEAR_INPUT;
	}

	final var resultText = app.getStrings().weatherFor() + " " + locationName + "\n" + weatherText;
	entries.add(new Entry(Entry.Type.RESULT, resultText, locationName));
	messageArea.clear();
	messageArea.add(resultText);
	area.refresh();
	app.setEventResponse(text(Sounds.DONE, resultText));
	return InputHandler.Result.CLEAR_INPUT;
    }

    @Override public void announceItem(Entry entry)
    {
	switch(entry.getType())
	{
	case QUERY:
	    app.setEventResponse(listItem(app.getLuwrain().getSpeakableText(entry.getText(), Luwrain.SpeakableTextType.NATURAL)));
	    break;
	case RESULT:
	    app.setEventResponse(listItem(app.getLuwrain().getSpeakableText(entry.getText(), Luwrain.SpeakableTextType.NATURAL)));
	    break;
	case ERROR:
	    app.setEventResponse(listItem(app.getLuwrain().getSpeakableText(entry.getText(), Luwrain.SpeakableTextType.NATURAL)));
	    break;
	}
    }

    @Override public String getTextAppearance(Entry entry)
    {
	switch(entry.getType())
	{
	case QUERY:
	    return app.getStrings().inputPrefix() + " " + entry.getText();
	case RESULT:
	    return entry.getText();
	case ERROR:
	    return "!!! " + entry.getText();
	}
	return entry.toString();
    }

    @Override public boolean onConsoleClick(ConsoleArea area, int index, Entry entry)
    {
	// TODO: по клику можно скопировать текст в буфер обмена или открыть детальный просмотр
	return true;
    }
}
