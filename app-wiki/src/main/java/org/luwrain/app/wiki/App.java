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

package org.luwrain.app.wiki;

import java.util.*;
import java.io.*;

import com.google.gson.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.io.api.mediawiki.*;
import org.luwrain.core.annotations.*;

@AppNoArgs(name = "wiki", title = { "en=Wiki", "ru=Вики" })
public final class App extends AppBase<Strings> implements MonoApp
{
    static private final String
	LOG_COMPONENT = "wiki",
	SETTINGS_PATH = "/org/luwrain/app/wiki";

    private final Gson gson = new Gson();
    private Config conf = null;
    private final String arg;
    final List<Server> servers = new ArrayList<>();
    final List<Page> pages = new ArrayList<>();
    private Conversations conv = null;
    private MainLayout mainLayout = null;

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.class, "luwrain.wiki");
	this.arg = arg;
    }

    @Override protected AreaLayout onAppInit()
    {
	conf = getLuwrain().loadConf(Config.class);
	if (conf == null)
	{
	    conf = new Config();
	    getLuwrain().saveConf(conf);
	}
	if (conf.servers != null)
	    servers.addAll(conf.servers);
	for(Server s: this.servers)
	{
	    if (s.name == null || s.name.trim().isEmpty())
		s.name = getStrings().defaultServerName();
	    if (s.searchUrl == null || s.searchUrl.trim().isEmpty())
		s.searchUrl = "https://en.wikipedia.org/w";
	    if (s.pagesUrl == null || s.pagesUrl.trim().isEmpty())
		s.pagesUrl = s.searchUrl;
	    s.name = s.name.trim();
	    s.searchUrl = s.searchUrl.trim();
	    s.pagesUrl = s.pagesUrl.trim();
	}
	this.conv = new Conversations(this);
	this.mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	return this.mainLayout.getAreaLayout();
    }

    boolean search(String query)
    {
	NullCheck.notEmpty(query, "query");
	final TaskId taskId = newTaskId();
	return runTask(taskId, ()->{
		final List<Page> res = new ArrayList<>();
		for(Server s: servers)
		{
		    final Mediawiki m = new Mediawiki(s.searchUrl);
		    try {
		    res.addAll(Arrays.asList(m.search(query)));
		    }
		    catch(Exception e)
		    {
			Log.error(LOG_COMPONENT, "unable to fetch wiki pages from " + s.searchUrl);
		    }
		}
		finishedTask(taskId, ()->{
			pages.clear();
			pages.addAll(res);
			getLuwrain().playSound(Sounds.OK);
			if (mainLayout != null)
			    mainLayout.area.refresh();
		    });
	    });
    }

    void saveServers()
    {
	conf.servers = servers;
	getLuwrain().saveConf(conf);
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

        @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	NullCheck.notNull(app, "app");
	mainLayout.area.moveHotPointToInput();
		return MonoApp.Result.BRING_FOREGROUND;
    }

    Conversations getConv() { return this.conv; }
}
