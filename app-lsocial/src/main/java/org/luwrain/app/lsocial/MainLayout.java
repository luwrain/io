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
import org.luwrain.io.api.lsocial.presentation.Presentation;
import org.luwrain.io.api.lsocial.presentation.Frame;
import org.luwrain.io.api.lsocial.publication.Publication;
import org.luwrain.io.api.lsocial.publication.Section;
import org.luwrain.app.lsocial.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;
import static org.luwrain.util.FileUtils.*;

public class MainLayout extends LayoutBase implements ListArea.ClickHandler<Object>
{
    static private final Logger log = LogManager.getLogger();

    final List<Object> entries = new ArrayList<>();
    protected final ListArea<Object> mainList;
    protected final Actions mainListActions;
    protected final App app;
    private LayoutExt ext = null;

    protected MainLayout(App app, boolean setAreas) 
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	mainList = new ListArea<Object>(listParams(p ->{
		    p.name = app.getStrings().appName();
		    p.model = new ListModel<Object>(entries);
		    p.appearance = new MainListAppearance(getControlContext());
		    p.clickHandler = this;
		}));
	setPropertiesHandler(mainList, a -> new OptionsLayout(app, getReturnAction()));

	mainListActions = actions(
				  action("insert", s.create(), new InputEvent(Special.INSERT), this::onMainListInsert));
	if (setAreas)
	    setAreaLayout(mainList, mainListActions);
    }

    @Override public boolean onListClick(ListArea<Object> area, int index, Object obj)
    {
	if (obj instanceof Publication publ)
	{
	    ext = new PublicationLayoutExt(this);
	    updateAreas();
	    ext.activateDefaultArea();
	}
	return true;
    }

boolean onSectClick(Section sect)
    {
	return true;
    }

boolean onFrameClick(Frame frame)
    {
	return true;
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
		    if (pr.getName().trim().isEmpty())
		    {
			app.message(app.getStrings().nameCannotBeEmpty(), Luwrain.MessageType.ERROR);
			return false;
		    }
		    final var taskId = app.newTaskId();
		    return app.runTask(taskId, () -> {
			    log.trace("Creating new presentation: " + pr);
			    final var resp = new org.luwrain.io.api.lsocial.presentation.CreateQuery(App.ENDPOINT)
			    .accessToken(app.conf.getAccessToken())
			    .name(pr.getName())
			    .title(pr.getTitle())
			    .authors(pr.getAuthors())
			    .subject(pr.getSubject())
			    .date(pr.getDate())
			    .exec();
			    log.trace("Responce is " + resp);
			});
	    });
	    app.setAreaLayout(layout);
	    getLuwrain().announceActiveArea();
	}
	}
	return true;
    }

    void updateAreas()
    {
	if (ext != null)
	{
	    log.debug("Using area layout of " + ext.getClass().getName());
	    ext.setLayout();
	} else
	setAreaLayout(mainList, mainListActions);
	app.setAreaLayout(this);
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
		app.setEventResponse(listItem(publ.getName() + " " + s.publicationListSuffix())); else
		if (item instanceof Presentation pr)
		    app.setEventResponse(listItem(pr.getName() + " " + s.presentationListSuffix())); else
		    app.setEventResponse(listItem(item.toString()));
	}

	@Override public String getNonSectionScreenAppearance(Object item)
	{
	    if (item instanceof Publication publ)
		return publ.getName();
	    if (item instanceof Presentation pr)
		return pr.getName();
	    return item.toString();
	}
    }
}
