/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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
import org.luwrain.controls.*;
import org.luwrain.io.download.Manager.Entry;

final class Appearance implements ListArea.Appearance
{
    private final Luwrain luwrain;
    private final Strings strings;

    Appearance(Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
    }

    @Override public void announceItem(Object item, Set<Flags> flags)
    {
	NullCheck.notNull(item, "item");
	NullCheck.notNull(flags, "flags");
	if (item instanceof Entry)
	{
	    final Entry entry = (Entry)item;
	    final Sounds sound;
	    final String text;
	    switch(entry.getStatus())
	    {
	    case RUNNING:
		text = luwrain.i18n().getNumberStr(entry.getPercent(), "percents") + " " + getName(entry);
		sound = null;
		break;
	    case SUCCESS:
		text = strings.statusOk() + " " + getName(entry);
		sound = Sounds.OK;
		break;
	    case FAILED:
		text = strings.statusFailure() + " " + getName(entry) + " " + entry.getErrorInfo();
		sound = Sounds.ATTENTION;
		break;
	    default:
		return;
	    }
	    if (sound != null)
		luwrain.setEventResponse(DefaultEventResponse.listItem(sound, text, Suggestions.LIST_ITEM)); else
		luwrain.setEventResponse(DefaultEventResponse.listItem(text, Suggestions.LIST_ITEM));
	    return;
	}
	luwrain.setEventResponse(DefaultEventResponse.listItem(item.toString(), Suggestions.LIST_ITEM));
    }

    @Override public String getScreenAppearance(Object item, Set<Flags> flags)
    {
	NullCheck.notNull(item, "item");
	NullCheck.notNull(flags, "flags");
		if (item instanceof Entry)
	{
	    final Entry entry = (Entry)item;
	    switch(entry.getStatus())
	    {
	    case RUNNING:
	    return "" + entry.getPercent() + "% " + getName(entry);
	    case SUCCESS:
		return strings.statusOk() + "% " + getName(entry);
	    case FAILED:
		return strings.statusFailure() + "% " + getName(entry) + " (" + entry.getErrorInfo() + ")";
	    default:
			return item.toString();
	    }			
	}
	return item.toString();
    }

    @Override public int getObservableLeftBound(Object item)
    {
	return 0;
    }

    @Override public int getObservableRightBound(Object item)
    {
	return getScreenAppearance(item, EnumSet.noneOf(Flags.class)).length();
    }

    private String getName(Entry entry)
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
}
