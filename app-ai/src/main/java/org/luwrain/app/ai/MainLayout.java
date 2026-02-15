// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai;

import java.util.*;
import java.io.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.console.*;
import org.luwrain.io.api.yandex_gpt.*;
import org.luwrain.app.ai.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleArea.*;
import static org.luwrain.util.FileUtils.*;

final class MainLayout extends LayoutBase  implements
					       ClickHandler<Entry>,
					       Appearance<Entry>,
					       InputHandler
{
    static private final Logger log = LogManager.getLogger();

    final List<Entry> entries = new ArrayList<>();
    final ConsoleArea<Entry> area;
    private final App app;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
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
	if (text.endsWith("..."))
	{
	    entries.add(new Entry(Entry.Type.USER, text.substring(0, text.length() - 3).trim(), null));
	return InputHandler.Result.CLEAR_INPUT;
	}
	if (text.startsWith("f "))
	{
	    entries.add(new Entry(Entry.Type.FILE, null, text.substring(2).trim()));
	return InputHandler.Result.CLEAR_INPUT;
	}
	entries.add(new Entry(Entry.Type.USER, text.trim(), null));
	try {
	for(var e: entries)
	    if (e.getType() == Entry.Type.FILE)
		e.setText(readTextFile(new File(e.getPath())));
	}
	catch(IOException ex)
	{
	    log.error(ex);
	    app.crash(ex);
	    return InputHandler.Result.OK;
	}
	final var messages = entries.stream()
	.map( e-> {
		final String type;
		switch(e.getType())
		{
		case USER:
		    		    		case FILE:
		    type = "user";
		    break;
		case MODEL:
		    type = "assistant";
		    break;
		    		default:
		    throw new IllegalArgumentException("Unknown entry type: " + e.getType().toString());
		}
		return new Message(type, e.getText());
	    }).toList();
	final var g = new YandexGpt(app.yandexConf.getFoundationModelsFolderId(), app.yandexConf.getFoundationModelsApiKey(),
				    new CompletionOptions(false, 0.7, 4096),
messages);
	final var taskId = app.newTaskId();
	app.runTask(taskId, ()-> {
	final var resp = g.doSync();
	final var a = resp.getResult().getAlternatives();
		final var m = a.get(0).getMessage();
		app.finishedTask(taskId, () -> {
		entries.add(new Entry(Entry.Type.MODEL, m.getText(), null));
		area.refresh();
		app.setEventResponse(text(Sounds.DONE, m.getText()));
		    });
	    });
	return InputHandler.Result.CLEAR_INPUT;
    }

    @Override public void announceItem(Entry entry)
    {

		switch(entry.getType())
	{
	    case USER:
		case MODEL:
app.setEventResponse(listItem(app.getLuwrain().getSpeakableText(entry.getText(), Luwrain.SpeakableTextType.NATURAL)));
break;
	case FILE:
	    app.setEventResponse(listItem(app.getStrings().filePrefix() + " " + app.getLuwrain().getSpeakableText(entry.getPath(), Luwrain.SpeakableTextType.PROGRAMMING)));
	    break;
    }
    }

    @Override public String getTextAppearance(Entry entry)
    {
	switch(entry.getType())
	{
	    case USER:
		case MODEL:
		    	return entry.getText();
	case FILE:
	    return entry.getPath();
    }
	return entry.toString();
    }

        @Override public boolean onConsoleClick(ConsoleArea area, int index, Entry entry)
    {
	return true;
    }
}
