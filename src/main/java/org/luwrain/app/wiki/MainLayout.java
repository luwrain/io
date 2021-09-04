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

package org.luwrain.app.wiki;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.io.api.mediawiki.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleUtils.*;

final class MainLayout extends LayoutBase
{
    private final App app;
final ConsoleArea area;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.area = new ConsoleArea(consoleParams((params)->{
		    params.model = new ListModel(app.pages);
		    params.appearance = new Appearance();
		    params.name = app.getStrings().appName();
	params.inputPos = ConsoleArea.InputPos.TOP;
		})) ;
    	area.setConsoleClickHandler((area,index,obj)->{
		if (obj == null || !(obj instanceof Page))
		    return false;
		final Page page = (Page)obj;
		try {
		    final String url = "https:" + URLEncoder.encode("", ""); ////" + URLEncoder.encode(page.lang) + ".wikipedia.org/wiki/" + URLEncoder.encode(page.title, "UTF-8").replaceAll("\\+", "%20");//Completely unclear why wikipedia doesn't recognize '+' sign
		    app.getLuwrain().launchApp("reader", new String[]{url});
		}
		catch (UnsupportedEncodingException e)
		{
		    app.crash(e);
		}
		return true;
	    });
	area.setConsoleInputHandler((area,text)->{
		NullCheck.notNull(text, "text");
		if (text.trim().isEmpty())
		    return ConsoleArea.InputHandler.Result.REJECTED;
		return app.search(text.trim())?ConsoleArea.InputHandler.Result.OK:ConsoleArea.InputHandler.Result.REJECTED;
	    });
	area.setInputPrefix(app.getStrings().appName() + ">");
	setAreaLayout(area, actions());
    }

final class Appearance implements ConsoleArea.Appearance
    {
	    @Override public void announceItem(Object item)
	    {
		NullCheck.notNull(item, "item");
		app.setEventResponse(listItem(item.toString()));
			    }
	    @Override public String getTextAppearance(Object item)
	    {
		NullCheck.notNull(item, "item");
		return item.toString();
	    }
}
}