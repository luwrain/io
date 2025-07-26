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

package org.luwrain.app.lsocial.layouts;

import java.util.*;
import java.text.*;
import java.util.function.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.lsocial.*;

import static java.util.Objects.*;

public final class NewEntryLayout extends LayoutBase
{
    enum Type { PR, PAPER, BOOK, THESIS, GRADUATION, COURSE };
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM YYYY г");

    final App app;
    final MainLayout mainLayout;
    final WizardArea wizard;
    final ActionHandler close;

    public NewEntryLayout(App app, MainLayout mainLayout, ActionHandler close)
    {
	super(app);
	this.app = app;
	this.mainLayout = mainLayout;
	this.close = close;
	final var s = app.getStrings();

	wizard = new WizardArea(getControlContext());
	wizard.setAreaName(s.newEntryAreaName());

	final var fr = wizard.newFrame()
	.addInput(s.nameEdit(), "")
	.addInput(s.subjectEdit(), "")
	.addInput(s.titleEdit(), "")
	.addInput(s.authorsEdit(), "")
	.addInput(s.dateEdit(), DATE_FORMAT.format(new Date()))
	.addClickable(s.presentationClickable(), v -> create(Type.PR, v))
	.addClickable(s.paperClickable(), v -> create(Type.PAPER, v))
	.addClickable(s.bookClickable(), v -> create(Type.BOOK, v))
	.addClickable(s.thesisClickable(), v -> create(Type.THESIS, v))
	.addClickable(s.graduationWorkClickable(), v -> create(Type.GRADUATION, v))
	.addClickable(s.courseWorkClickable(), v -> create(Type.COURSE, v));

	wizard.show(fr);
	setCloseHandler(close);
	setAreaLayout(wizard, null);
    }

    boolean create(Type type, WizardArea.WizardValues values)
    {
	final String
	name = values.getText(0).trim(),
	subject = values.getText(1).trim(),
	title = values.getText(2).trim(),
	authors = values.getText(3).trim(),
	date = values.getText(4);
	if (name.isEmpty())
	{
	    app.message(app.getStrings().nameCannotBeEmpty(), Luwrain.MessageType.ERROR);
	    return true;
	}

	if (type == Type.PR)
	{
	    final var taskId = app.newTaskId();
	    return app.runTask(taskId, () -> {
		    final var resp = new org.luwrain.io.api.lsocial.presentation.CreateQuery(App.ENDPOINT)
		    .accessToken(app.conf.getAccessToken())
		    .name(name)
		    .title(title)
		    .authors(authors)
		    .subject(subject)
		    .date(date)
		    .exec();
		    final var res = mainLayout.fetchMainListItems();
		    app.finishedTask(taskId, () -> {
			    mainLayout.entries.clear();
			    mainLayout.entries.addAll(res);
			    mainLayout.mainList.refresh();
			    close.onAction();
			});
		});
	}

	final int t;
	switch(type)
	{
	case PAPER:
	    t = org.luwrain.io.api.lsocial.publication.Publication.TYPE_PAPER;
	    break;
	case BOOK:
	    t = org.luwrain.io.api.lsocial.publication.Publication.TYPE_BOOK;
	    break;
	case THESIS:
	    t = org.luwrain.io.api.lsocial.publication.Publication.TYPE_THESIS;
	    break;
	case GRADUATION:
	    t = org.luwrain.io.api.lsocial.publication.Publication.TYPE_GRADUATION_WORK;
	    break;
	case COURSE:
	    t = org.luwrain.io.api.lsocial.publication.Publication.TYPE_COURSE_WORK;
	    break;
	default:
	    return false;
	}

	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		final var resp = new org.luwrain.io.api.lsocial.publication.CreateQuery(App.ENDPOINT)
		.accessToken(app.conf.getAccessToken())
		.type(t)
		.name(name)
		.title(title)
		.authors(authors)
		.subject(subject)
		.date(date)
		.exec();
		final var res = mainLayout.fetchMainListItems();
		app.finishedTask(taskId, () -> {
			mainLayout.entries.clear();
			mainLayout.entries.addAll(res);
			mainLayout.mainList.refresh();
			close.onAction();
		    });
	    });
    }
}
