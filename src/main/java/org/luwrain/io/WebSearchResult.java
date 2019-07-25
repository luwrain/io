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

package org.luwrain.io;

import org.luwrain.core.*;

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
}
