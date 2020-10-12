/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.io.json;

import java.util.*;
import com.google.gson.*;
import com.google.gson.annotations.*;

import org.luwrain.core.*;

public final class MessageContent
{
    @SerializedName("to")
    public String to = "";

    @SerializedName("cc")
    public String cc = "";

    @SerializedName("subject")
    public String subject = "";

    @SerializedName("text")
    public List<String> text;

    @SerializedName("attachments")
    public List<String> attachments = null;

    public MessageContent()
    {
    }

    public MessageContent(String to, String cc, String subject, String[] text)
    {
	NullCheck.notNull(to, "to");
	NullCheck.notNull(cc, "cc");
	NullCheck.notNull(subject, "subject");
	NullCheck.notNullItems(text, "text");
	this.to = to;
	this.cc = cc;
	this.subject = subject;
	this.text = Arrays.asList(text);
	this.attachments = Arrays.asList(new String[0]);
    }

    public MessageContent(String to, String cc, String subject, String text, String lineSep)
    {
	this(to, cc, subject, text.split(lineSep, -1));
    }

        public MessageContent(String to, String cc, String subject, String text)
    {
	this(to, cc, subject, text, System.lineSeparator());
    }


    public String[] getTextAsArray()
    {
	if (text == null)
	    return new String[0];
	return text.toArray(new String[text.size()]);
    }

    public String[] getAttachmentsAsArray()
    {
	if (attachments == null)
	    return new String[0];
	return attachments.toArray(new String[attachments.size()]);
    }

    public String toJson()
    {
	return new Gson().toJson(this);
    }

    static public MessageContent fromJson(String text)
    {
	NullCheck.notNull(text, "text");
	if (text.isEmpty())
	    return null;
	return new Gson().fromJson(text, MessageContent.class);
    }
}
