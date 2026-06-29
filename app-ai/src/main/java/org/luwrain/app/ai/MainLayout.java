// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.console.*;
import org.luwrain.io.ai.*;
import org.luwrain.app.ai.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleArea.*;
import static org.luwrain.util.FileUtils.*;

final class MainLayout extends LayoutBase  implements
					       ListArea.ClickHandler<Profile>,
					       ConsoleArea.ClickHandler<Entry>,
					       ConsoleArea.Appearance<Entry>,
					       InputHandler
{
    static private final Logger log = LogManager.getLogger();

    final App app;
    final List<Profile> profiles = new ArrayList<>();
    final List<Entry> entries = new ArrayList<>();
    final ListArea<Profile> profilesArea;
    final ConsoleArea<Entry> consoleArea;
    final SimpleArea messageArea;


    MainLayout(App app)
    {
	super(app);
	this.app = app;

		this.profilesArea = new ListArea<>(listParams(p -> {
		    p.model = new org.luwrain.controls.list.ListModel<Profile>(profiles);
		    p.name = app.getStrings().profilesAreaName();
		    p.clickHandler = this;
		}));


	this.consoleArea = new ConsoleArea<Entry>(consoleParams(p ->{
		    p.name = app.getStrings().appName();
		    p.model = new ListModel<Entry>(entries);
		    p.appearance = this;
		    p.inputHandler = this;
		    p.clickHandler = this;
		    p.inputPos = ConsoleArea.InputPos.TOP;
		    p.inputPrefix = app.getStrings().inputPrefix();
		}));
		this.messageArea = new SimpleArea(getControlContext(), app.getStrings().appName());
		//	setPropertiesHandler(area, a -> new OptionsLayout(app, getReturnAction()));
	/*
	final Actions actions = actions(
	    action("profiles", app.getStrings().actionProfiles(), new InputEvent(InputEvent.Special.F9), () -> actProfiles())
	);
	*/
		setAreaLayout(AreaLayout.LEFT_TOP_BOTTOM,
			      profilesArea, actions(actNewProfile(), actDeleteProfile()),
			      consoleArea, null,
			      messageArea, null);
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
	final var completion = Completion.newInstance(getLuwrain().loadConf(org.luwrain.settings.ai.Config.class));
	entries.forEach(e -> {
		switch(e.getType())
		{
		case USER:
		case FILE:
		    completion.addUserMessage(e.getText());
		    break;
		case MODEL:
		    completion.addAssistantMessage(e.getText());
		    break;
		}
	    });
	final var taskId = app.newTaskId();
	final var res = completion.querySincSingle();
	app.runTask(taskId, ()-> {
		app.finishedTask(taskId, () -> {
			if (res != null)
			{
			    entries.add(new Entry(Entry.Type.MODEL, res, null));
			    messageArea.clear();
			    messageArea.add(res);
			}
			area.refresh();
			app.setEventResponse(text(Sounds.DONE, res));
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

        @Override public boolean onListClick(ListArea<Profile> area, int index, Profile profile)
    {
	if (profile == null)
	    return false;
	final var editLayout = new ProfileEditLayout(app, profile, () -> {
		profilesArea.refresh();
		app.setAreaLayout(this);
		app.getLuwrain().announceActiveArea();
		return true;
	    });
	app.setAreaLayout(editLayout);
	app.getLuwrain().announceActiveArea();
	return true;
    }

    private ActionInfo actNewProfile()
    {
	return action("new-profile", app.getStrings().actionNewProfile(), new InputEvent(InputEvent.Special.INSERT), () -> {
		final String name = app.conv.newProfileName();
		if (name == null)
		    return true;
		final Profile p = new Profile();
		p.setName(name.trim());
		p.setTemperature(Profile.DEFAULT_TEMPERATURE);
		profiles.add(p);
		app.getLuwrain().saveConf(app.conf);
		profilesArea.refresh();
		profilesArea.select(p, false);
		return true;
	    });
    }

    private ActionInfo actDeleteProfile()
    {
	return action("delete-profile", app.getStrings().actionDeleteProfile(), new InputEvent(InputEvent.Special.DELETE), () -> {
		final Profile profile = profilesArea.selected();
		if (profile == null)
		    return false;
		if (!app.conv.confirmProfileDeleting(profile))
		    return true;
		profiles.remove(profile);
		app.conf.getProfiles().remove(profile);
		app.getLuwrain().saveConf(app.conf);
		profilesArea.refresh();
		return true;
	    });
    }

}
