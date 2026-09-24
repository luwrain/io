// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.url;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;
import static org.luwrain.util.TextUtils.*;

@AppSingleArg(
	      name = "url",
	      title = { "en=URL", "ru=URL" }
	      )
public final class App extends AppBase<Strings>
{
    private final String argText;
    private Conv conv = null;
    private MainLayout mainLayout = null;
    String[] text = new String[0];

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.class, "luwrain.url");
	this.argText = arg;
    }

    @Override protected AreaLayout onAppInit()
    {
	this.conv = new Conv(this);
	if (argText != null)
	    this.text = splitLines(argText);
	this.mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	return this.mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    Conv getConv()
    {
	return this.conv;
    }
}
