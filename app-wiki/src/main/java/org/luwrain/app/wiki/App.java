// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

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
    private final Gson gson = new Gson();
    Config conf = null;
    private final String arg;
    final List<Page> pages = new ArrayList<>();
    Conv conv = null;
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
	if (conf.servers == null)
	    conf.servers = new ArrayList<>();
	for(Server s: this.conf.servers)
	{
	    if (s.getName() == null || s.getName().trim().isEmpty())
		s.setName(getStrings().defaultServerName());
	    if (s.getSearchUrl() == null)
		s.setSearchUrl("");
	    if (s.getPagesUrl() ==  null)
		s.setPagesUrl("");
	    s.setName(s.getName().trim());
	    s.setSearchUrl(s.getSearchUrl().trim());
	    s.setPagesUrl(s.getPagesUrl().trim());
	}
	this.conv = new Conv(this);
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
		for(Server s: this.conf.servers)
		{
		    final Mediawiki m = new Mediawiki(s.getSearchUrl());
		    try {
		    res.addAll(Arrays.asList(m.search(query)));
		    }
		    catch(Exception e)
		    {
			crash(e);
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
}
