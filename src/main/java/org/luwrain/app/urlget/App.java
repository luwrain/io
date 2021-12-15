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
import static org.luwrain.util.TextUtils.*;

public final class App extends AppBase<Strings>
{
    private final String argText;
    private Conversations conv = null;
    private MainLayout mainLayout = null;
    String[] text = new String[0];

    public App() { this(null); }
    public App(String arg)
    {
	super(Strings.NAME, Strings.class, "luwrain.wiki");
	this.argText = arg;
    }

    @Override protected AreaLayout onAppInit()
    {
	this.conv = new Conversations(this);
	if (argText != null)
	    this.text = splitLinesAnySeparator(argText);
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
