/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.nlp.ru;

import org.luwrain.core.*;
import org.luwrain.script.*;

public final class Token extends EmptyHookObject
{
    public enum Type {NUM, LATIN, CYRIL, SPACE, PUNC};

    public final Type type;
    public final String text;

    public Token(Type type, String text)
    {
	NullCheck.notNull(type, "type");
	NullCheck.notEmpty(text, "text");
	this.type = type;
	this.text = text;
    }

    public Type getType()
    {
	return type;
    }

    public String getText(String text)
    {
	return text;
    }

    @Override public String toString()
    {
	return text;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "type":
	    return type.toString().toLowerCase();
	case "text":
	    return text;
	default:
	    return super.getMember(name);
	}
    }
}
