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

package org.luwrain.io.api.duckduckgo;

import java.io.*;
import java.util.*;
import java.net.*;

import com.google.gson.*;
import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

public final class InstantAnswer
{
    static private final String CHARSET = "UTF-8";

    static private Gson gson = null;

    public enum Flags {}

    public Answer getAnswer(String query, Properties props, Set<Flags> flags) throws IOException
    {
	NullCheck.notEmpty(query, "query");
	NullCheck.notNull(props, "props");
	NullCheck.notNull(flags, "flags");
	final StringBuilder b = new StringBuilder();
	b.append("https://api.duckduckgo.com/?q=")
	.append(URLEncoder.encode(query, CHARSET))
.append("&format=json");
	if (props.getProperty("kl") != null && !props.getProperty("kl").isEmpty())
	    b.append("&kl=").append(props.getProperty("kl"));
	final URL url = new URL(new String(b));
	final URLConnection con;
	try {
 con = Connections.connect(url.toURI(), 0);
	}
	catch(URISyntaxException e)
	{
	    throw new IOException(e);
	}
	try (final BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), CHARSET))) {
	    return gson.fromJson(r, Answer.class);
	}
    }

    static public final class RelatedTopic
    {
	@SerializedName("Text")
	private String text = null;
	@SerializedName("FirstURL")
	private String firstUrl = null;
	public String getText()
	{
	    return text;
	}
	public String getFirstUrl()
	{
	    return firstUrl;
	}
	@Override public String toString()
	{
	    return text != null?text:"";
	}
    }

    static public final class Answer
    {
static public final  String 
    TYPE_A = "a",
    TYPE_D = "d";
    @SerializedName("Type")
	private String type = null;
	@SerializedName("Heading")
	private String heading = null;
	@SerializedName("AbstractText")
	private String absText = null;
	@SerializedName("RelatedTopics")	
	private List<RelatedTopic> relatedTopics;
	public String getType()
	{
	    return type;
	}
	public String getAbsText()
	{
	    return absText;
	}
	public String getHeading()
	{
	    return heading;
	}
	public RelatedTopic[] getRelatedTopics()
	{
	    return relatedTopics != null?relatedTopics.toArray(new RelatedTopic[relatedTopics.size()]):null;
	}
}
}
