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
    enum Side {LEFT, RIGHT};

    final String startFrom;
    private Conv conv = null;
    private MainLayout mainLayout = null;

    public App(String startFrom)
    {
	super(Strings.NAME, Strings.class, "luwrain.commander");
	if (startFrom != null && !startFrom.isEmpty())
	    this.startFrom = startFrom; else
	    this.startFrom = null;
    }
    public App() { this(null); }

    @Override public AreaLayout onAppInit()
    {
	//	this.sett = Settings.create(getLuwrain());
	this.conv = new Conv(this);
	//	this.hooks = new Hooks(this);
	this.mainLayout = new MainLayout(this);
	//	this.operationsLayout = new OperationsLayout(this);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    Conv getConv() { return this.conv; }
}
