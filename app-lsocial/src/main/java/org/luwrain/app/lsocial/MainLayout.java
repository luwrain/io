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
import java.io.*;
//import java.net.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.console.*;
import org.luwrain.io.api.yandex_gpt.*;
import org.luwrain.app.lsocial.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleArea.*;
import static org.luwrain.util.FileUtils.*;

final class MainLayout extends LayoutBase  implements
					       ClickHandler<Entry>,
					       Appearance<Entry>,
					       InputHandler
{
    static private final Logger log = LogManager.getLogger();

    final List<Entry> entries = new ArrayList<>();
    final ConsoleArea<Entry> area;
    private final App app;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.area = new ConsoleArea<Entry>(consoleParams(p ->{
		    p.name = app.getStrings().appName();
		    p.model = new ListModel<Entry>(entries);
		    p.appearance = this;
		    p.inputHandler = this;
		    p.clickHandler = this;
		    p.inputPos = ConsoleArea.InputPos.TOP;
		}));
	setPropertiesHandler(area, () -> new OptionsLayout(app, getReturnAction()));
	setAreaLayout(area, null);
    }

    @Override public InputHandler.Result onConsoleInput(ConsoleArea area, String text)
    {
		return InputHandler.Result.CLEAR_INPUT;
    }

    @Override public void announceItem(Entry entry)
    {
    }

    @Override public String getTextAppearance(Entry entry)
    {
	switch(entry.getType())
	{
	    case USER:
		case MODEL:
		    	return entry.getText();
	case FILE:
	    return entry.getPath();
    }
	return entry.toString();
    }

        @Override public boolean onConsoleClick(ConsoleArea area, int index, Entry entry)
    {
	return true;
    }
}
