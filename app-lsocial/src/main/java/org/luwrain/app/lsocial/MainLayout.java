/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.lsocial;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.io.api.yandex_gpt.*;
import org.luwrain.io.api.lsocial.publication.*;
import org.luwrain.io.api.lsocial.publication.*;
import org.luwrain.app.lsocial.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;
import static org.luwrain.util.FileUtils.*;

final class MainLayout extends LayoutBase 
{
    static private final Logger log = LogManager.getLogger();

    final List<Object> entries = new ArrayList<>();
    final ListArea<Object> mainList;
    private final App app;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	this.mainList = new ListArea<Object>(listParams(p ->{
		    p.name = app.getStrings().appName();
		    		    p.model = new ListModel<Object>(entries);
		    		    p.appearance = new MainListAppearance(getControlContext());
		}));
		setPropertiesHandler(mainList, a -> new OptionsLayout(app, getReturnAction()));
		final var mainListActions = actions(
						    action("insert", s.create(), new InputEvent(Special.INSERT), this::onMainListInsert));
	setAreaLayout(mainList, mainListActions);
    }

        private boolean onMainListInsert()
    {
	final var type = app.conv.newMainListItemType();
	if (type == null)
	    return true;
	switch(type)
	{
	case PRES: {
	    final var layout = new NewPresentationLayout(app, getReturnAction(), pr -> {
		    return true;
	    });
	    app.setAreaLayout(layout);
	    getLuwrain().announceActiveArea();
	}
	    
	}
	return true;
    }

    void updateMainList()
    {
	final var taskId = app.newTaskId();
	app.runTask(taskId, ()-> {
		log.trace("Starting updating the main list");
				final var res = new ArrayList<Object>();
		log.trace("Querying presentations");
		final var prRes = new org.luwrain.io.api.lsocial.presentation.ListQuery(App.ENDPOINT).accessToken(app.conf.getAccessToken()).exec();
		log.debug("Response: " + prRes.getStatus());
				log.debug("Response: " + prRes.getNumTotal());
		res.addAll(prRes.getEn());
		log.trace("Querying publications");
				final var publRes = new org.luwrain.io.api.lsocial.publication.ListQuery(App.ENDPOINT).accessToken(app.conf.getAccessToken()).exec();
				res.addAll(publRes.getEn());
		app.finishedTask(taskId, ()-> {
			entries.clear();
			entries.addAll(res);
			mainList.refresh();
		    });
	    });
    }

final class MainListAppearance  extends DoubleLevelAppearance<Object>
    {
	MainListAppearance(ControlContext context) { super(context); }
	@Override public boolean isSectionItem(Object obj)
	{
	    return false;
	}

	    @Override public void announceNonSection(Object item)
	{
	    final var s = app.getStrings();
	    if (item instanceof Publication publ)
		app.setEventResponse(listItem(publ.getName() + " " + s.publicationListSuffix()));
	}

	    @Override public String getNonSectionScreenAppearance(Object item)
	{
	    if (item instanceof Publication publ)
		return publ.getName();
	    return item.toString();
	}
    }
}
