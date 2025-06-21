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

package org.luwrain.io.api.mediawiki;

import java.util.*;
import java.io.*;
import java.net.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import org.luwrain.core.*;

public final class Mediawiki
{
    private final String baseUrl;

    public Mediawiki(String baseUrl)
    {
	NullCheck.notEmpty(baseUrl, "baseUrl");
	if (baseUrl.endsWith("/"))
	this.baseUrl = baseUrl; else
	    this.baseUrl = baseUrl + "/";
    }

    public Page[] search(String q) throws IOException
    {
	final String url = baseUrl + "api.php?action=query&list=search&srsearch=" + URLEncoder.encode(q, "UTF-8") + "&format=xml";
	final Connection con = Jsoup.connect(url);
	final Document doc = con.get();
	final Elements pages = doc.getElementsByTag("p");
	final List<Page> res = new ArrayList<>();
	for(Element page: pages)
	{
	    final String title = page.attr("title");
	    String comment = page.attr("snippet");
	    if (title == null || title.isEmpty())
		continue;
	    if (comment == null)
		comment = "";
	    comment = stripTags(comment);
	    res.add(new Page(baseUrl, title, comment));
	}
	return res.toArray(new Page[res.size()]);
    }

    static private String stripTags(String s)
    {
	return s.replaceAll("</span>", "").replaceAll("<span class=.searchmatch.>", "");
    }
}
