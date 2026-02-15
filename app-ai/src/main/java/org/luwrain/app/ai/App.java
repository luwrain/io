// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

@AppNoArgs(
	   name = "ai",
	   title = { "en=GPT", "ru=GPT"}, 
	   category = StarterCategory.COMMUNICATIONS)
public final class App extends AppBase<Strings>
{
    public Conv conv = null;
    public Config conf = null;
    org.luwrain.settings.yandex.Config yandexConf;
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
	        yandexConf = getLuwrain().loadConf(org.luwrain.settings.yandex.Config.class);
	if (yandexConf == null)
	{
	    yandexConf = new org.luwrain.settings.yandex.Config();
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
