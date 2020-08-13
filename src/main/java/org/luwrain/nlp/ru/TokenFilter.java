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

public final class TokenFilter extends EmptyHookObject
{
    private final Token[][] tokens;
    private final boolean[] optional;

    static private RomanNum romanNum = null;

    TokenFilter(Token[][] tokens, boolean[] optional)
    {
	this.tokens = tokens;
	this.optional = optional;
	if (tokens.length != optional.length)
	    throw new IllegalArgumentException("tokens and optional must have the save size");
    }

    public int size()
    {
	return tokens.length;
    }

    public Token[] getTokens(int index)
    {
	return this.tokens[index];
    }

    public boolean getOptional(int index)
    {
	return this.optional[index];
    }

    public boolean match(Token[] tokens)
    {
	return match(tokens, 0);
    }

    public boolean matchBefore(Token[] tokens, int index)
    {
	if (index < size())
	    return false;
	return match(tokens, index - size());
    }

    public boolean match(Token[] tokens, int offset)
    {
	if (offset >= tokens.length)
	    return false;
	if (tokens.length - offset < size())
	    return false;
	for(int i = 0;i < this.tokens.length;i++)
	{
	    if (match(this.tokens[i], tokens[i + offset]))
		continue;
	    if (!this.optional[i])
		return false;
	}
	return true;
    }

    private boolean match(Token[] filter, Token token)
    {
	for(Token f: filter)
	    if (match(f, token))
		return true;
	return false;
    }

    private boolean match(Token filter, Token token)
    {
	switch(filter.type)
	{
	case CYRIL:
	    return token.type == Type.CYRIL && filter.text.equals(token.text.toUpperCase());
	    	case NUM:
	    return token.type == Type.NUM && filter.text.equals(token.text);
	    	    	case PUNC:
	    return token.type == Type.PUNC && filter.text.equals(token.text);
	case LATIN:
	    if (Character.isUpperCase(filter.text.charAt(0)))
		return matchClass(filter.text.toUpperCase(), token);
	    return token.type == Type.LATIN && filter.text.toUpperCase().equals(token.text.toUpperCase());
	case SPACE:
	    return token.type == Type.SPACE;
	default:
	    return false;
	    

	    
	}
    }

    private boolean matchClass(String className, Token token)
    {
	switch(className)
	{
	case "NUM":
	    return token.type == Type.NUM;
	case "RN":
	    if (token.type != Type.LATIN)
		return false;
	    if (romanNum == null)
		romanNum = new RomanNum();
	    return romanNum.find(token.text) >= 0;
	default:
	    return false;
	}
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "match":
	    return (BiPredicate)this::matchHook;
	default:
	    return super.getMember(name);
	}
    }

    private boolean matchHook(Object a1, Object a2)
    {
	if (a1 == null || a2 == null)
	    return false;
	final List l = ScriptUtils.getArray(a1);
	if (l == null)
	    return false;
	final List<Token> tokens = new LinkedList();
	for(Object o: l)
	    if (o instanceof Token)
		tokens.add((Token)o); else
		return false;
	final Number index = ScriptUtils.getNumberValue(a2);
	if (index == null)
	    return false;
	return match(tokens.toArray(new Token[tokens.size()]), index.intValue());
    }
}
