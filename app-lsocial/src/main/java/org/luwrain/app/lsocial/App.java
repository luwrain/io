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
import org.luwrain.io.api.yandex_gpt.*;

import static java.util.Objects.*;
import static org.luwrain.util.TextUtils.*;

@AppNoArgs(name = "social", title = {"en=LUWRAIN Social"})
public final class App extends AppBase<Strings>
{
    static public final String ENDPOINT = "https://luwrain.social";

    public Conv conv = null;
    public Config conf = null;
    private MainLayout mainLayout = null;
    private Set<String> nbspAfterWords = new HashSet<>(Arrays.asList("В", "НА", "ПОД"));

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

    List<String> yandexGpt(String query) throws IOException
    {
	final var messages = List.of(new Message("user", query));
	final var g = new YandexGpt(conf.getYandexFolderId(), conf.getYandexApiKey(),
				    new CompletionOptions(false, 0.7, 4096),
messages);
	final var resp = g.doSync();
	final var a = resp.getResult().getAlternatives();
	return Arrays.asList(a.get(0).getMessage().getText().split("\n", -1));
    }

    public String translateUserInput(String line, int pos, String text)
    {
	final var lastWord = getLastWord(line, pos).toUpperCase();
	if (text.equals(" ") && nbspAfterWords.contains(lastWord))
	    return "~";
	return text;
    }

    
}
