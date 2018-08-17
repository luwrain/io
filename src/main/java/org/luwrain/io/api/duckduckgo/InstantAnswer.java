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

package org.luwrain.io.api.duckduckgo;

import java.io.*;
import java.util.*;
import java.net.*;
import org.json.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

public final class InstantAnswer
{
    static private final String ENCODING = "UTF-8";

    public enum Flags {}

    public Answer getAnswer(String query, Properties props, Set<Flags> flags) throws IOException
    {
	NullCheck.notEmpty(query, "query");
	NullCheck.notNull(props, "props");
	NullCheck.notNull(flags, "flags");
	final StringBuilder b = new StringBuilder();
	b.append("https://api.duckduckgo.com/?q=");
	b.append(URLEncoder.encode(query, ENCODING));
	b.append("&format=json");
	if (props.getProperty("kl") != null && !props.getProperty("kl").isEmpty())
	    b.append("&kl=" + props.getProperty("kl"));

	final URL url = new URL(new String(b));
	final URLConnection con = Connections.connect(url, 0);
	final InputStream is = con.getInputStream();
			final JSONTokener t = new JSONTokener(is);
		final JSONObject obj = new JSONObject(t);
		final String typeValue = obj.getString("Type");
		final Answer.Type type;
		switch(typeValue.toLowerCase().trim())
		{
		case "a":
		    type = Answer.Type.A;
		    break;
		case "d":
		    type = Answer.Type.D;
		    break;
		default:
		    type = Answer.Type.NONE;
		}
		final String absText = obj.getString("AbstractText");
		return new Answer(type, absText);
    }

    static public final class Answer
    {
	public enum Type {A, D, NONE};
	private final Type type;
	private final String absText;
	Answer(Type type, String absText)
	{
	    NullCheck.notNull(type, "type");
	    NullCheck.notNull(absText, "absText");
	    this.type = type;
	    this.absText = absText;
	}
	public Type getType()
	{
	    return type;
	}
	public String getAbsText()
	{
	    return absText;
	}
    }
}
