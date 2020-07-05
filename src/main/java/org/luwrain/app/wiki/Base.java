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

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.script.*;

final class Base
{
    final Luwrain luwrain;
    final Strings strings;
    private FutureTask task;
    private Page[] searchResult = new Page[0];

    Base(Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
    }

    boolean search(String lang, String query, ConsoleArea area)
    {
	NullCheck.notNull(lang, "lang");
	NullCheck.notEmpty(query, "query");
	NullCheck.notNull(area, "area");
	if (task != null && !task.isDone())
	    return false;
	task = createTask(area, lang, query);
	luwrain.executeBkg(task);
	luwrain.onAreaNewBackgroundSound(area);
	return true;
    }

    boolean isBusy()
    {
	return task != null && !task.isDone();
    }

    private Page[] query(String query)
    {
	NullCheck.notEmpty(query, "query");
	final AtomicReference res = new AtomicReference();
	luwrain.xRunHooks("luwrain.wiki.search", (hook)->{
		try {
		    final Object obj = hook.run(new Object[]{query});
		    if (obj == null)
			return Luwrain.HookResult.CONTINUE;
		    res.set(obj);
		    return Luwrain.HookResult.BREAK;
		}
		catch(RuntimeException e)
		{
		    res.set(e);
		    return Luwrain.HookResult.BREAK;
		}
	    });
	if (res.get() == null)
	    return new Page[0];
	if (res.get() instanceof RuntimeException)
	    throw (RuntimeException)res.get();
	final List objs = ScriptUtils.getArray(res.get());
	if (objs == null)
	    return new Page[0];
	final List<Page> pages = new LinkedList();
	for(Object o: objs)
	{
	    final Object langObj = ScriptUtils.getMember(o, "lang");
	    final Object titleObj = ScriptUtils.getMember(o, "title");
	    final Object commentObj = ScriptUtils.getMember(o, "comment");
	    final String lang = ScriptUtils.getStringValue(langObj);
	    final String title = ScriptUtils.getStringValue(titleObj);
	    final String comment = ScriptUtils.getStringValue(commentObj);
	    if (lang == null || title == null || comment == null)
		continue;
	    pages.add(new Page(lang, title, comment));
	}
	return pages.toArray(new Page[pages.size()]);
    }

    ConsoleArea.Model getModel()
    {
	NullCheck.notNullItems(searchResult, "searchResult");
	return new ConsoleArea.Model(){
	    @Override public int getItemCount()
	    {
		NullCheck.notNullItems(searchResult, "searchResult");
		return searchResult.length;
	    }
	    @Override public Object getItem(int index)
	    {
		if (index < 0 || index >= searchResult.length)
		    throw new IllegalArgumentException("Illegal index value (" + index + ")");
		return searchResult[index];
	    }
	};
    }

    ConsoleArea.Appearance getAppearance()
    {
	return new ConsoleArea.Appearance(){
	    @Override public void announceItem(Object item)
	    {
		NullCheck.notNull(item, "item");
		if (!(item instanceof Page))
		    return;
		luwrain.playSound(Sounds.LIST_ITEM);
		final Page page = (Page)item ;
		luwrain.speak(item.toString());
	    }
	    @Override public String getTextAppearance(Object item)
	    {
		NullCheck.notNull(item, "item");
		return item.toString();
	    }
	};
    }

        private FutureTask createTask(ConsoleArea area, String lang, String query)
    {
	NullCheck.notNull(area, "area");
	NullCheck.notNull(lang, "lang");
	NullCheck.notNull(query, "query");
	return new FutureTask(()->{
		try {
this.searchResult = query(query);
		}
		catch(RuntimeException e)
		{
		    luwrain.crash(e);
		}
		luwrain.runUiSafely(()->{
			task = null;
	luwrain.onAreaNewBackgroundSound(area);
	area.refresh();
	area.reset(false);
		    });
	}, null);
    }
}
