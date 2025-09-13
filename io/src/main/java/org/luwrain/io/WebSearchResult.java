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

package org.luwrain.io;

import java.util.*;

import org.luwrain.core.*;
import static org.luwrain.script.ScriptUtils.*;

public final class WebSearchResult
{
    private final String title;
    private final Item[] items;

    public WebSearchResult(String title, Item[] items)
    {
	NullCheck.notNull(title, "title");
	NullCheck.notNullItems(items, "items");
	this.title = title;
	this.items = items;
    }

    public String getTitle()
    {
	return title;
    }

    public Item[] getItems()
    {
	return items.clone();
    }

    public Item getItem(int index)
    {
	return items[index];
    }

    public int getItemCount()
    {
	return items.length;
    }

    public boolean noItems()
    {
	return items.length == 0;
    }

    static public final class Item
    {
	private final String title;
	private final String snippet;
	private final String displayUrl;
	private final String clickUrl;
	public Item(String title, String snippet, String displayUrl, String clickUrl)
	{
	    NullCheck.notNull(title, "title");
	    NullCheck.notNull(snippet, "snippet");
	    NullCheck.notNull(displayUrl, "displayUrl");
	    NullCheck.notNull(clickUrl, "clickUrl");
	    this.title = title;
	    this.snippet = snippet;
	    this.displayUrl = displayUrl;
	    this.clickUrl = clickUrl;
	}
	public String getTitle()
	{
	    return title;
	}
	public String getSnippet()
	{
	    return snippet;
	}
	public String getDisplayUrl()
	{
	    return displayUrl;
	}
	public String getClickUrl()
	{
	    return clickUrl;
	}
	@Override public String toString()
	{
	    return title;
	}
    }

    static public Item[] getItemsFromHookObj(Object itemsObj)
    {
	if (itemsObj == null)
	    return null;
	final Object[] items = asArray(itemsObj);
	if (items == null)
	    return null;
	final List<Item> res = new ArrayList<>();
	for(Object o: items)
	    if (o != null)
	    {
		final String
		title = asString(getMember(o, "title")),
		snippet = asString(getMember(o, "snippet")),
		displayUrl = asString(getMember(o, "displayUrl")),
		clickUrl = asString(getMember(o, "clickUrl"));
		if (title == null || title.trim().isEmpty())
		    continue;
		res.add(new Item(title.trim(),
						 snippet != null?snippet.trim():"",
						 displayUrl != null?displayUrl.trim():"",
						 clickUrl != null?clickUrl.trim():""));
	    }
	return res.toArray(new WebSearchResult.Item[res.size()]);
    }
}
