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

package org.luwrain.app.urlget;

import java.util.*;
import java.io.*;

import com.google.gson.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.io.api.mediawiki.*;

public final class App extends AppBase<Strings>
{
    static private final String SETTINGS_PATH = "/org/luwrain/app/wiki";

    private final Gson gson = new Gson();
    private Settings sett = null;
    private final String arg;
    private Conversations conv = null;
    private MainLayout mainLayout = null;

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.NAME, Strings.class, "luwrain.wiki");
	this.arg = arg;
    }

    @Override protected AreaLayout onAppInit()
    {
	this.conv = new Conversations(this);
	this.mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	return this.mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    Conversations getConv() { return this.conv; }

}
