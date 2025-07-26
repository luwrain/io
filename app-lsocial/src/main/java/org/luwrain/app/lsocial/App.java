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

package org.luwrain.app.lsocial;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

import static java.util.Objects.*;

@AppNoArgs(name = "social", title = {"en=LUWRAIN Social"})
public final class App extends AppBase<Strings>
{
    static public final String ENDPOINT = "https://luwrain.social";

    public Conv conv = null;
    public Config conf = null;
    private MainLayout mainLayout = null;

    public App() { super(Strings.class, "luwrain.commander"); }

    @Override public AreaLayout onAppInit()
    {
	conf = getLuwrain().loadConf(Config.class);
	if (conf == null)
	{
	    conf = new Config();
	    getLuwrain().saveConf(conf);
	}
	conv = new Conv(this);
	mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	if (!requireNonNull(conf.getAccessToken(), "").trim().isEmpty())
	    mainLayout.updateMainList();
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }
}
