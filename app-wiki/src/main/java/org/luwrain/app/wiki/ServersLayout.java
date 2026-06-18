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

final class ServersLayout extends LayoutBase
{
static private final String
    NAME = "name",
    SEARCH__URL = "search-url",
    PAGES_URL = "pages-url";

    private final App app;
    final ListArea<Server> serversArea;
    final FormArea paramsArea;
    private final List<Server> servers = new ArrayList<>();

    ServersLayout(App app, ActionHandler closing)
    {
	super(app);
	this.app = app;
	this.servers.addAll(app.servers);
	this.serversArea = new ListArea<>(listParams((params)->{
		    params.model = new ListModel<>(servers);
		    params.name = app.getStrings().serversAreaName();
		})) ;
	final Actions serversActions = actions(
					       action("new-server", app.getStrings().actionNewServer(), new InputEvent(InputEvent.Special.INSERT), this ::actNewServer)
					       );
	this.paramsArea = new FormArea(getControlContext(), app.getStrings().serverParamsAreaName());
	final Actions paramsActions = actions();
	setAreaLayout(AreaLayout.TOP_BOTTOM, serversArea, serversActions, paramsArea, paramsActions);
	setOkHandler(()->{
		app.servers.clear();
		app.servers.addAll(this.servers);
		app.saveServers();
		return closing.onAction();
	    });
	setCloseHandler(closing);
    }

    private boolean actNewServer()
    {
	final String name = app.conv.newServerName();
	if (name == null)
	    return true;
	final Server s = new Server();
	s.setName(name.trim());
	s.setSearchUrl("");
	s.setPagesUrl("");
	servers.add(s);
	app.conf.servers.add(s);
	app.getLuwrain().saveConf(app.conf);
	serversArea.refresh();
	serversArea.select(s, false);
	return true;
    }
}
