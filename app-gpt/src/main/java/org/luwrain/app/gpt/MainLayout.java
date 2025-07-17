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
import java.io.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.console.*;
import org.luwrain.app.gpt.layouts.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase  implements
					       ConsoleArea.ClickHandler<Entry>,
					       ConsoleArea.Appearance<Entry>
{
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
		    p.clickHandler = this;
		    p.inputPos = ConsoleArea.InputPos.TOP;
		    p.inputPrefix = app.getStrings().inputPrefix();
		}));
	setPropertiesHandler(area, () -> new OptionsLayout(app, getReturnAction()));
		setAreaLayout(area, null);
    }

    @Override public void announceItem(Entry entry)
    {
	app.setEventResponse(listItem(app.getLuwrain().getSpeakableText(entry.getMessage(), Luwrain.SpeakableTextType.NATURAL)));
    }

    @Override public String getTextAppearance(Entry entry)
    {
	return entry.getMessage();
    }

        @Override public boolean onConsoleClick(ConsoleArea area, int index, Entry entry)
    {
	return true;
    }
}
