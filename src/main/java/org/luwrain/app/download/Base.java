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

import org.luwrain.core.*;
import org.luwrain.linux.wifi.*;
import org.luwrain.controls.*;
import org.luwrain.io.download.Manager.Entry;

final class Base
{
    private final Luwrain luwrain;
    private final Strings strings;
    final org.luwrain.io.download.Manager manager;
    final Conversations conv;

    private Entry[] entries = new Entry[0];

    Base(Luwrain luwrain, Strings strings, org.luwrain.io.download.Manager manager)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(manager, "manager");
	this.luwrain = luwrain;
	this.strings = strings;
	this.manager = manager;
	this.conv = new Conversations(luwrain, strings);
	refresh();
    }

    void refresh()
    {
	this.entries = manager.getAllEntries();
    }

    ListArea.Model getListModel()
    {
	return new ListModel();
    }

    private final class ListModel implements ListArea.Model
    {
	@Override public int getItemCount()
	{
	    return entries != null?entries.length:0;
	}
	@Override public Object getItem(int index)
	{
	    return entries != null?entries[index]:null;
	}
	@Override public void refresh()
	{
	    Base.this.refresh();
	}
    }
}
