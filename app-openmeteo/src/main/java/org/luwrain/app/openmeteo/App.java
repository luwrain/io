// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.openmeteo;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

@AppNoArgs(
	   name = "openmeteo",
	   title = { "en=Open-Meteo", "ru=Open-Meteo"},
	   category = StarterCategory.COMMUNICATIONS)
public final class App extends AppBase<Strings>
{
    public WeatherQuery weatherQuery = null;
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
	weatherQuery = new WeatherQuery(this);
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
