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
import org.luwrain.io.api.lsocial.publication.Publication;
import org.luwrain.io.api.lsocial.publication.Section;
import org.luwrain.app.lsocial.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;
import static org.luwrain.app.base.LayoutBase.*;

class PublLayoutExt implements LayoutExt
{
    static private final Logger log = LogManager.getLogger();

    final App app;
    final MainLayout mainLayout;
    final Publication publ;
    final List<Section> sections = new ArrayList<>();
    final ListArea<Section> sectList;
    final Actions actions;

    PublLayoutExt(MainLayout mainLayout, Publication publ)
    {
	this.app = mainLayout.app;
	this.mainLayout = mainLayout;
	this.publ = publ;
	final var s = mainLayout.app.getStrings();
	if (publ.getSects() != null)
	    sections.addAll(publ.getSects());

	sectList = new ListArea<Section>(mainLayout.listParams(p ->{
		    p.name = s.appName();
		    p.model = new ListModel<Section>(sections);
		    p.clickHandler = (area, index, sect) -> onSectClick(sect);
		}));

	actions = mainLayout.actions(
			  mainLayout.action("insert", s.create(), new InputEvent(Special.INSERT), this::onInsert)
);
    }

    boolean onInsert()
    {
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		new org.luwrain.io.api.lsocial.publication.CreateSectionQuery(App.ENDPOINT)
		.accessToken(app.conf.getAccessToken())
		.publ(String.valueOf(publ.getId()))
		.type(org.luwrain.io.api.lsocial.publication.Section.TYPE_MARKDOWN)
				.source("")
		.exec();
	    });
    }

    boolean onSectClick(Section sect)
    {
	final var index = sectList.selectedIndex();
	if (index < 0)
	    return false;
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		final var res = new org.luwrain.io.api.lsocial.publication.GetSectionQuery(App.ENDPOINT)
		.accessToken(app.conf.getAccessToken())
		.publ(publ)
		.sect(index)
		.exec();
		app.finishedTask(taskId, () -> {
			final var e = new PublSectLayoutExt(this, publ, res.getSect(), index);
	mainLayout.openExt(e);
		    });
	    });
	    }

    @Override public void setLayout()
    {
	mainLayout.setAreaLayout(AreaLayout.LEFT_RIGHT, mainLayout.mainList, mainLayout.mainListActions,
				 sectList, actions);
    }

    @Override public void activateDefaultArea()
    {
	mainLayout.setActiveArea(sectList);
    }
}
