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

import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.lsocial.*;

import static java.util.Objects.*;

public final class NewPresentationLayout extends LayoutBase
{
    static private final String
	NAME = "name",
	TITLE = "title",
	AUTHORS = "authors",
	SUBJECT = "subject",
	DATE = "date";

    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM YYYY г");

    final App app;
    final FormArea form;

    public NewPresentationLayout(App app, ActionHandler close, Predicate<org.luwrain.io.api.lsocial.presentation.Presentation> okFunc)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), s.newPresentationAreaName());
	form.addEdit(NAME, s.nameEdit(), "");
	form.addEdit(TITLE, s.titleEdit(), "");
	form.addEdit(AUTHORS, s.authorsEdit(), "");
	form.addEdit(SUBJECT, s.subjectEdit(), "");
	form.addEdit(DATE, s.dateEdit(), DATE_FORMAT.format(new Date()));
	setAreaLayout(form, null);
	setOkHandler(() -> {
		final var p = new org.luwrain.io.api.lsocial.presentation.Presentation();
		if (okFunc.test(p))
		    close.onAction();
		return true;
	    });
	setCloseHandler(close);
    }
}
