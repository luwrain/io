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

package org.luwrain.app.gpt;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

@AppNoArgs(title = {"ru=GPTT"}, name = "gpt")
public final class App extends AppBase<Strings>
{
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
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }
}
