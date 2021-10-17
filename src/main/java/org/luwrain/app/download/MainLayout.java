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

package org.luwrain.app.download;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.io.download.Manager.Entry;
import org.luwrain.app.base.*;
import org.luwrain.controls.ListUtils.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase
{
    private final App app;
    final ListArea<Entry> listArea;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.listArea = new ListArea<Entry>(listParams((params)->{
		    params.model = new ListModel<>(app.entries);
		    params.appearance = new Appearance();
		    params.name = app.getStrings().appName();
		}) ){
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != SystemEvent.Type.REGULAR )
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case CLIPBOARD_PASTE:
			return app.onClipboardPaste();
		    default:
			return super.onSystemEvent(event);
		    }
		}
	    };
	final Actions actions = actions(
					);
	setAreaLayout(listArea, actions);
    }

    static private String getName(Entry entry)
    {
	NullCheck.notNull(entry, "entry");
	final String fileName = entry.getUrl().getFile();
	if (fileName != null && !fileName.isEmpty())
	{
	    final int slash = fileName.lastIndexOf("/");
	    if (slash >= 0 && slash + 1 < fileName.length())
		return fileName.substring(slash + 1);
	    return fileName;
	}
	return entry.getUrl().toString();
    }

    private final class Appearance extends ListUtils.AbstractAppearance<Entry>
    {
	@Override public void announceItem(Entry entry, Set<Flags> flags)
	{
	    NullCheck.notNull(entry, "entry");
	    NullCheck.notNull(flags, "flags");
	    final Sounds sound;
	    final String text;
	    switch(entry.getStatus())
	    {
	    case RUNNING:
		text = getLuwrain().i18n().getNumberStr(entry.getPercent(), "percents") + " " + getName(entry);
		sound = null;
		break;
	    case SUCCESS:
		text = app.getStrings().statusOk() + " " + getName(entry);
		sound = Sounds.SELECTED;
		break;
	    case FAILED:
		text = app.getStrings().statusFailure() + " " + getName(entry) + " " + entry.getErrorInfo();
		sound = Sounds.ALERT;
		break;
	    default:
		return;
	    }
	    if (sound != null)
		app.setEventResponse(listItem(sound, text, Suggestions.LIST_ITEM)); else
		app.setEventResponse(listItem(text, Suggestions.LIST_ITEM));
	    return;
	}
	@Override public String getScreenAppearance(Entry entry, Set<Flags> flags)
	{
	    NullCheck.notNull(entry, "entry");
	    NullCheck.notNull(flags, "flags");
	    switch(entry.getStatus())
	    {
	    case RUNNING:
		return String.valueOf(entry.getPercent()) + "% " + getName(entry);
	    case SUCCESS:
		return app.getStrings().statusOk() + "% " + getName(entry);
	    case FAILED:
		return app.getStrings().statusFailure() + "% " + getName(entry) + " (" + entry.getErrorInfo() + ")";
	    default:
		return entry.toString();
	    }			
	}
    }
}
