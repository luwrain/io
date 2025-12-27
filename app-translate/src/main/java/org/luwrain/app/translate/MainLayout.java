// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.translate;

import java.util.*;
import java.io.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.io.api.yandex.translate.*;
import org.luwrain.io.api.yandex.translate.model.*;
import org.luwrain.app.translate.layouts.*;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.luwrain.core.DefaultEventResponse.*;
//import static org.luwrain.controls.ConsoleArea.*;
//import static org.luwrain.util.FileUtils.*;

final class MainLayout extends LayoutBase 
{
    static private final Logger log = LogManager.getLogger();

    final EditArea firstArea, secondArea;
    final App app;

    MainLayout(App app)
    {
	super(app);
	final var s = app.getStrings();
	this.app = app;
	this.firstArea = new EditArea(editParams(p -> {
		    p.name = s.firstLangAreaName();
		})){
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    if (event.getType() != SystemEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case OK:
			return translate(true);
		    default:
			return super.onSystemEvent(event);
		    }
		}};
	this.secondArea = new EditArea(editParams(p ->{
		    p.name = s.secondLangAreaName();
		})){
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    if (event.getType() != SystemEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case OK:
			return translate(false);
		    default:
			return super.onSystemEvent(event);
		    }
		}};
	setPropertiesHandler(firstArea, a -> new OptionsLayout(app, getReturnAction()));
	setAreaLayout(AreaLayout.LEFT_RIGHT, firstArea, null, secondArea, null);
    }
    
    boolean translate(boolean fromFirstArea)
    {
	final List<String> text = fromFirstArea?firstArea.getTextAsList():secondArea.getTextAsList();
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		final var req = new Request(app.yandexConf.getTranslatorFolderId(), "en", List.of(text.stream().collect(joining("\n"))));
					final var t = new YandexTranslate(app.yandexConf.getTranslatorApiKey());
					final var resp = t.request(req);
		app.finishedTask(taskId, () -> {
			if (resp != null && resp.getTranslations() != null && !resp.getTranslations().isEmpty())
			{
			    secondArea.update((lines, hotPoint) -> {
				    for(var tr: resp.getTranslations())
					lines.addLine(requireNonNullElse(tr.getText(), ""));
				    return true;
				});
			}
		    });		
	    });
    }

}
