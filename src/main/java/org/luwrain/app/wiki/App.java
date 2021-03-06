/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.wiki;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;
import org.luwrain.popups.*;

public final class App implements Application, MonoApp
{
    private Luwrain luwrain = null;
    private Base base = null;
    private Strings strings = null;
    private ConsoleArea area;

    final String arg;

    public App()
    {
	this.arg = "";
    }

    public App(String arg)
    {
	NullCheck.notNull(arg, "arg");
	this.arg = arg;
    }

    @Override public InitResult onLaunchApp(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final Object o = luwrain.i18n().getStrings(Strings.NAME);
	if (o == null || !(o instanceof Strings))
	    return new InitResult(InitResult.Type.NO_STRINGS_OBJ, Strings.NAME);
	this.strings = (Strings)o;
	this.luwrain = luwrain;
	this.base = new Base(luwrain, strings);
	createArea();
	if (!arg.trim().isEmpty())
	{
	    base.search("", arg.trim(), area);
	    area.setInput(arg.trim());
	}
	return new InitResult();
    }

    private void createArea()
    {
	final ConsoleArea.Params params = new ConsoleArea.Params();
	params.context = new DefaultControlContext(luwrain);
	params.model = base.getModel();
	params.appearance = base.getAppearance();
	params.name = strings.appName();
	params.inputPos = ConsoleArea.InputPos.TOP;
	area = new ConsoleArea(params){
		@Override public boolean onInputEvent(InputEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case ESCAPE:
			    closeApp();
			    return true;
			}
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != SystemEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case CLOSE:
			closeApp();
			return true;
		    default:
			return super.onSystemEvent(event);
		    }
		}
		@Override public boolean onAreaQuery(AreaQuery query)
		{
		    NullCheck.notNull(query, "query");
		    switch(query.getQueryCode())
		    {
		    case AreaQuery.BACKGROUND_SOUND:
			if (base.isBusy())
			{
			    ((BackgroundSoundQuery)query).answer(new BackgroundSoundQuery.Answer(BkgSounds.FETCHING));
			    return true;
			}
			return false;
		    default:
			return super.onAreaQuery(query);
		    }
		}
	    };
	area.setConsoleClickHandler((area,index,obj)->{
		if (obj == null || !(obj instanceof Page))
		    return false;
		final Page page = (Page)obj;
		try {
		    final String url = "https://" + URLEncoder.encode(page.lang) + ".wikipedia.org/wiki/" + URLEncoder.encode(page.title, "UTF-8").replaceAll("\\+", "%20");//Completely unclear why wikipedia doesn't recognize '+' sign
		    luwrain.launchApp("reader", new String[]{url});
		}
		catch (UnsupportedEncodingException e)
		{
		    luwrain.crash(e);
		}
		return true;
	    });
	area.setConsoleInputHandler((area,text)->{
		NullCheck.notNull(text, "text");
		if (text.trim().isEmpty() || base.isBusy())
		    return ConsoleArea.InputHandler.Result.REJECTED;
		base.search("", text.trim(), area);
		return ConsoleArea.InputHandler.Result.OK;
	    });
	area.setInputPrefix(strings.appName() + ">");
    }

    @Override public String getAppName()
    {
	return strings.appName();
    }

    @Override public AreaLayout getAreaLayout()
    {
	return new AreaLayout(area);
    }

    @Override public void closeApp()
    {
	luwrain.closeApp();
    }

    @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	NullCheck.notNull(app, "app");
	if (!(app instanceof App))
	    return MonoApp.Result.BRING_FOREGROUND;
	final App wikiApp = (App)app;
	base.search("", wikiApp.arg.trim(), area);
	area.setInput(wikiApp.arg.trim());
	return MonoApp.Result.BRING_FOREGROUND;
    }
}
