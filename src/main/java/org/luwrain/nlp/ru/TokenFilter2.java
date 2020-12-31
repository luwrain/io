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

package org.luwrain.nlp.ru;

import java.util.*;
import java.util.function.*;

import org.luwrain.core.*;
import org.luwrain.nlp.ru.Token.Type;
import org.luwrain.nlp.RomanNum;

import org.luwrain.script.*;

public final class TokenFilter2 extends EmptyHookObject
{
    private final TokenPlaceholder[] placeholders;

    static private RomanNum romanNum = null;

    TokenFilter2(Token[][] tokens, boolean[] optional)
    {
	if (tokens.length != optional.length)
	    throw new IllegalArgumentException("tokens and optional must have the save size");
	this.placeholders = new TokenPlaceholder[tokens.length];
	for(int i = 0;i < tokens.length;i++)
	    this.placeholders[i] = new TokenPlaceholder(tokens[i], optional[i]);
    }

    public int size()
    {
	return placeholders.length;
    }

    public int match(Token[] tokens)
    {
	return match(tokens, 0);
    }

    public boolean matchBefore(Token[] tokens, int index)
    {
	if (index < placeholders.length)
	    return false;
	return match(tokens, index - size()) > 0;
    }

        public int match(Token[] tokens, int offset)
    {
	if (offset >= tokens.length)
	    return 0;
	int pos = 0;
	for(int i = 0;i < this.placeholders.length;i++)
	{
	    if (pos + offset >= tokens.length)
		return 0;
	    if (this.placeholders[i].match(tokens[pos + offset]))
	    {
		pos++;
		continue;
	    }
	    if (!this.placeholders[i].optional)
		return 0;
	}
	return pos;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "match":
	    return (BiFunction)this::matchHook;
	case "length":
	    return placeholders.length;
	default:
	    return super.getMember(name);
	}
    }

    private Integer matchHook(Object a1, Object a2)
    {
	if (a1 == null || a2 == null)
	    return new Integer(0);
	final List l = ScriptUtils.getArray(a1);
	if (l == null)
	    return new Integer(0);
	final List<Token> tokens = new LinkedList();
	for(Object o: l)
	    if (o instanceof Token)
		tokens.add((Token)o); else
		return new Integer(0);
	final Number index = ScriptUtils.getNumberValue(a2);
	if (index == null)
	    return new Integer(0);
	return new Integer(match(tokens.toArray(new Token[tokens.size()]), index.intValue()));
    }
}
