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

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	consoleArea = new ConsoleArea<NominatimPlace>(consoleParams(p -> {
		    p.model = new ListModel<>(elements);
		    p.clickHandler = (area, index, el) -> onClick(el);
		    p.inputHandler = (area, text) -> onInput(text);
		    p.appearance = this;
		}));
	setAreaLayout(consoleArea, null);
    }

    boolean onClick(NominatimPlace place)
    {
	return false;
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
}
