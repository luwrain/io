// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bsky;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

import static java.util.Objects.*;
import static org.luwrain.util.TextUtils.*;

@AppNoArgs(
	   name = "bs",
	   title = { "en=BlueSky", "ru=BlueSky" },
	   category = StarterCategory.COMMUNICATIONS)
public final class App extends AppBase<Strings>
{
    static public final String ENDPOINT = "https://bsky.social";

    public Config conf = null;
    MainLayout mainLayout = null;
    private GreetingLayout greetingLayout = null;
    private FollowingsLayout followingsLayout = null;

    public App() { super(Strings.class, "luwrain.bs"); }

    @Override public AreaLayout onAppInit() throws Exception
    {
	conf = getLuwrain().loadConf(Config.class);
	if (conf == null)
	{
	    conf = new Config();
	    getLuwrain().saveConf(conf);
	}
	mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	if (requireNonNullElse(conf.getHandle(), "").trim().isEmpty()
	    || requireNonNullElse(conf.getAppPassword(), "").trim().isEmpty())
	{
	    greetingLayout = new GreetingLayout(this);
	    return greetingLayout.getAreaLayout();
	}
	mainLayout.updateRecords();
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    public String translateUserInput(String line, int pos, String text)
    {
	// BlueSky doesn't need special input translation
	return text;
    }

    public boolean okAnswer(String type, String message)
    {
	if (type.equals("OK"))
	    return true;
	message(message, Luwrain.MessageType.ERROR);
	return false;
    }

    boolean isReady()
    {
	return conf != null
	    && !requireNonNullElse(conf.getHandle(), "").trim().isEmpty()
	    && !requireNonNullElse(conf.getAppPassword(), "").trim().isEmpty();
    }

    public FollowingsLayout getFollowingsLayout()
    {
	if (followingsLayout == null)
	    followingsLayout = new FollowingsLayout(this);
	return followingsLayout;
    }
}
