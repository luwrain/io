// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

import org.luwrain.io.api.osm.model.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase implements ConsoleArea.Appearance<Element>
{
    private final App app;
    final List<Element> elements = new ArrayList<>();
    final ConsoleArea<Element> consoleArea;
    
    MainLayout(App app)
    {
	super(app);
	this.app = app;
	consoleArea = new ConsoleArea<Element>(consoleParams(p -> {
		    p.model = new ListModel<>(elements);
		    p.clickHandler = (area, index, el) -> onClick(el);
		    p.inputHandler = (area, text) -> onInput(text);
		    p.appearance = this;
		}));
	setAreaLayout(consoleArea, null);
    }

    boolean onClick(Element el)
    {
	return false;
    }

    ConsoleArea.InputHandler.Result onInput(String text)
    {
	if (text.isEmpty())
	    return ConsoleArea.InputHandler.Result.REJECTED;
	final var taskId = app.newTaskId();
	if (app.runTask(taskId, () -> {
		    final var n = app.osm.findByName("node", text);
		    app.finishedTask(taskId, () -> {
			    elements.clear();
			    if (n != null)
			    {
			    elements.addAll(n);
			    getLuwrain().playSound(Sounds.DONE);
			    }			     else
							    app.setEventResponse(text(Sounds.DONE, app.getStrings().nothingFound()));
			    consoleArea.refresh();
			});
		}))
	    return ConsoleArea.InputHandler.Result.OK;
	    	    return ConsoleArea.InputHandler.Result.REJECTED;
    }

        @Override public void announceItem(Element el)
    {
	final var tags = el.getTags();
	if (tags != null)
	{
	    final var name = tags.get("name");
	    if (name != null && !name.trim().isEmpty())
	    app.setEventResponse(listItem(name));
	}
    }

    @Override public String getTextAppearance(Element el)
    {
		final var tags = el.getTags();
	if (tags != null)
	    return requireNonNullElse(tags.get("name"), "");
	return "";
	    }

}
