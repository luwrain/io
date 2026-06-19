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
		})) ;
	setAreaLayout(serversArea, actions(actNewServer()));
	setOkHandler(()->{
		app.conf.servers.clear();
		app.conf.servers.addAll(this.servers);
		app.getLuwrain().saveConf(app.conf);
		return closing.onAction();
	    });
	setCloseHandler(closing);
    }

    private ActionInfo actNewServer()
    {
	return action("new-server", app.getStrings().actionNewServer(), new InputEvent(InputEvent.Special.INSERT), () -> {
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
	    });
    }
}
