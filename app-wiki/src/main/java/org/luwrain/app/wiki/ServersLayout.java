// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.wiki;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.io.api.mediawiki.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ListUtils.*;

final class ServersLayout extends LayoutBase implements ListArea.ClickHandler<Server>
{
    static private final String
	DEFAULT_SEARCH_URL = "https://en.wikipedia.org/w/",
	DEFAULT_PAGES_URL = "https://en.wikipedia.org/wiki/";

    private final App app;
    private final List<Server> servers = new ArrayList<>();
    final ListArea<Server> serversArea;

    ServersLayout(App app, ActionHandler closing)
    {
	super(app);
	this.app = app;
	this.servers.addAll(app.conf.servers);
	this.serversArea = new ListArea<>(listParams(p -> {
		    p.model = new ListModel<>(servers);
		    p.name = app.getStrings().serversAreaName();
		    p.clickHandler = this;
		}));
	setAreaLayout(serversArea, actions(actNewServer(), actDeleteServer()));
	setOkHandler(()->{
		app.conf.servers.clear();
		app.conf.servers.addAll(this.servers);
		app.getLuwrain().saveConf(app.conf);
		return closing.onAction();
	    });
	setCloseHandler(closing);
    }

    @Override public boolean onListClick(ListArea<Server> area, int index, Server server)
    {
	if (server == null)
	    return false;
	final var editLayout = new ServerEditLayout(app, server, () -> {
		serversArea.refresh();
		app.setAreaLayout(this);
		app.getLuwrain().announceActiveArea();
		return true;
	    });
	app.setAreaLayout(editLayout);
	app.getLuwrain().announceActiveArea();
	return true;
    }

    private ActionInfo actNewServer()
    {
	return action("new-server", app.getStrings().actionNewServer(), new InputEvent(InputEvent.Special.INSERT), () -> {
		final String name = app.conv.newServerName();
		if (name == null)
		    return true;
		final Server s = new Server();
		s.setName(name.trim());
		s.setSearchUrl(DEFAULT_SEARCH_URL);
		s.setPagesUrl(DEFAULT_PAGES_URL);
		servers.add(s);
		app.conf.servers.add(s);
		app.getLuwrain().saveConf(app.conf);
		serversArea.refresh();
		serversArea.select(s, false);
		return true;
	    });
    }

    private ActionInfo actDeleteServer()
    {
	return action("delete-server", app.getStrings().actionDeleteServer(), new InputEvent(InputEvent.Special.DELETE), () -> {
		final Server server = serversArea.selected();
		if (server == null)
		    return false;
		if (!app.conv.confirmServerDeleting(server))
		    return true;
		servers.remove(server);
		app.conf.servers.remove(server);
		app.getLuwrain().saveConf(app.conf);
		serversArea.refresh();
		return true;
	    });
    }
}
