// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.wiki;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.wiki.*;

import static java.util.Objects.*;

public final class ServerEditLayout extends LayoutBase
{
    static private final String
	NAME = "name",
	SEARCH_URL = "searchUrl",
	PAGES_URL = "pagesUrl";

    final App app;
    final FormArea form;
    final Server server;

    public ServerEditLayout(App app, Server server, ActionHandler close)
    {
	super(app);
	this.app = app;
	this.server = server;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), s.serverParamsAreaName());
	form.addEdit(NAME, s.nameEdit(), requireNonNullElse(server.getName(), ""));
	form.addEdit(SEARCH_URL, s.searchUrlEdit(), requireNonNullElse(server.getSearchUrl(), ""));
	form.addEdit(PAGES_URL, s.pagesUrlEdit(), requireNonNullElse(server.getPagesUrl(), ""));
	setAreaLayout(form, null);
	setOkHandler(() -> {
		server.setName(form.getEnteredText(NAME).trim());
		server.setSearchUrl(form.getEnteredText(SEARCH_URL).trim());
		server.setPagesUrl(form.getEnteredText(PAGES_URL).trim());
		app.getLuwrain().saveConf(app.conf);
		close.onAction();
		return true;
	    });
	setCloseHandler(close);
    }
}
