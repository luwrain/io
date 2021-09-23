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

package org.luwrain.app.download;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.io.download.Manager.Entry;
import org.luwrain.app.base.*;

public class App extends AppBase<Strings> implements MonoApp, Runnable
{
    private final org.luwrain.io.download.Manager manager;
    final List<Entry> entries = new ArrayList<>();
    private MainLayout mainLayout = null;

    public App(org.luwrain.io.download.Manager manager)
    {
	super(Strings.NAME, Strings.class, "luwrain.download");
	NullCheck.notNull(manager, "manager");
	this.manager = manager;
    }

    @Override public AreaLayout onAppInit()
    {
		this.manager.addChangesListener(this);
		this.mainLayout = new MainLayout(this);
		setAppName(getStrings().appName());
		return this.mainLayout.getAreaLayout();
    }

    @Override public void run()
    {
	getLuwrain().runUiSafely(()->this.mainLayout.listArea.refresh());
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    boolean onClipboardPaste()
    {
	final Object[] objs = getLuwrain().getClipboard().get();
	for(Object obj: objs)
	{
	    if (obj instanceof URL)
	    {
		final URL url = (URL)obj;
		try {
		    this.manager.addDownload(url, suggestDestFile(url));
		}
		catch(IOException e)
		{
		    message(getStrings().downloadAddingError(getLuwrain().i18n().getExceptionDescr(e)), Luwrain.MessageType.ERROR);
		    return true;
		}
		continue;
	    }
	    final URL url;
	    try {
		url = new URL(obj.toString());
	    }
	    catch(MalformedURLException e)
	    {
		message(getStrings().unableToMakeUrl(obj.toString()), Luwrain.MessageType.ERROR);
		return true;
	    }
	    try {
		this.manager.addDownload(url, suggestDestFile(url));
	    }
	    catch(IOException e)
	    {
		message(getStrings().downloadAddingError(getLuwrain().i18n().getExceptionDescr(e)), Luwrain.MessageType.ERROR);
		return true;
	    }
	}
	return true;
    }

    private File suggestDestFile(URL url)
    {
	NullCheck.notNull(url, "url");
	final File destDir = new File("/tmp");
	final String path = url.getFile();
	if (path == null || path.isEmpty())
	    return new File(destDir, simplify(url.toString()));
	final int lastSlash = path.lastIndexOf("/");
	final int lastBackslash = path.lastIndexOf("\\");
	final int pos = Math.max(lastSlash, lastBackslash);
	if (pos < 0)
	    return new File(destDir, path);
	if (pos + 1 >= path.length())
	    return new File(destDir, simplify(url.toString()));
	return new File(destDir, path.substring(pos + 1));
    }

    private String simplify(String str)
    {
	NullCheck.notNull(str, "str");
	final StringBuilder b = new StringBuilder();
	boolean wasNonDash = false;
	for(int i = 0;i < str.length();++i)
	{
	    final char c = str.charAt(i);
	    if (Character.isLetter(c) || Character.isDigit(c))
	    {
		b.append("" + c);
		wasNonDash = true;
		continue;
	    }
	    if (wasNonDash)
		b.append("-");
	    wasNonDash = false;
	}
	final String res = new String(b);
	return !res.isEmpty()?res:"-";
    }


        void refreshEntries()
    {
	this.entries.clear();
	this.entries.addAll(Arrays.asList(manager.getAllEntries()));
    }

        @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	NullCheck.notNull(app, "app");
	return MonoApp.Result.BRING_FOREGROUND;
    }


    @Override public void closeApp()
    {
	this.manager.removeChangesListener(this);
	super.closeApp();
    }
}
