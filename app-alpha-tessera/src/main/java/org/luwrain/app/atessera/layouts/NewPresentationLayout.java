// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

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
		p.setName(form.getEnteredText(NAME));
		p.setTitle(form.getEnteredText(TITLE));
		p.setAuthors(form.getEnteredText(AUTHORS));
		p.setSubject(form.getEnteredText(SUBJECT));
		p.setDate(form.getEnteredText(DATE));
		if (okFunc.test(p))
		    close.onAction();
		return true;
	    });
	setCloseHandler(close);
    }
}
