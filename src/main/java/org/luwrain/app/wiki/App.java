/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

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

public final class App extends AppBase<Strings> implements MonoApp
{
    static private final String SETTINGS_PATH = "/org/luwrain/app/wiki";

    private final Gson gson = new Gson();
    private Settings sett = null;
    private final String arg;
    final List<Server> servers = new ArrayList<>();
    final List<Page> pages = new ArrayList<>();
    private MainLayout mainLayout = null;

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.NAME, Strings.class);
	this.arg = arg;
    }

    @Override protected AreaLayout onAppInit()
    {
	this.sett = RegistryProxy.create(getLuwrain().getRegistry(), SETTINGS_PATH, Settings.class);
	final List<Server> serv = gson.fromJson(this.sett.getServers(""), Server.LIST_TYPE);
	if (serv != null)
	    this.servers.addAll(serv);
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
		    res.addAll(Arrays.asList(m.search(query)));
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

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

        @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	NullCheck.notNull(app, "app");
		return MonoApp.Result.BRING_FOREGROUND;
    }

    interface Settings
    {
	String getServers(String defValue);
	void setServers(String value);
    }
}
