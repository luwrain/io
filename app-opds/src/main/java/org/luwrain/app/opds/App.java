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

package org.luwrain.app.opds;

import java.util.*;
import java.io.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.app.opds.Opds.Entry;
import org.luwrain.core.annotations.*;

@AppNoArgs(name = "opds",
	   title = { "en=Libraries", "ru=Библиотеки"},
	   category = StarterCategory.COMMUNICATIONS)
public final class App extends AppBase<Strings>
{
    final List<RemoteLibrary> libraries = new ArrayList();
    final List<Entry> entries = new ArrayList<Entry>();
    final LinkedList<HistoryItem> history = new LinkedList<HistoryItem>();

    private Config conf= null;
    private Conversations conv = null;
    private MainLayout mainLayout = null;

    public App()
    {
	super(Strings.class);
    }

    @Override protected AreaLayout onAppInit()
    {
	conf = getLuwrain().loadConf(Config.class);
	if (conf == null)
	    conf = new Config();
	this.conv = new Conversations(this);
	loadLibraries();
	this.mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    boolean open(URL url)
    {
	final TaskId taskId = newTaskId();
	return runTask(taskId, ()->{
		final Opds.Result res = Opds.fetch(url);
		finishedTask(taskId, ()->{
			if (res.error == Opds.Result.Errors.FETCHING_PROBLEM)
			{
			    message("Невозможно подключиться к серверу или данные по указанному адресу не являются правильным OPDS-каталогом", Luwrain.MessageType.ERROR);//FIXME:
			    return;
			}
			if(res.hasEntries())
			{
			    entries.clear();
			    entries.addAll(Arrays.asList(res.getEntries()));
			    history.add(new HistoryItem(url, res.getEntries()));
			    mainLayout.listArea.refresh();
			    mainLayout.listArea.reset(false);
			    mainLayout.setActiveArea(mainLayout.listArea);
			}
		    });
	    });
    }

    private void loadLibraries()
    {
	this.libraries.clear();
	if (conf.libraries != null)
	    libraries.addAll(conf.libraries);
	Collections.sort(libraries);
    }

    void saveLibraries()
    {
	conf.libraries = libraries;
	getLuwrain().saveConf(conf);
    }

    URL opened()
    {
	return !history.isEmpty()?history.getLast().url:null;
    }

    Conversations getConv()
    {
	return conv;
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }
}
