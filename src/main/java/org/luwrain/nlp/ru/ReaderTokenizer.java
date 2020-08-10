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
import java.io.*;

import org.luwrain.core.*;

public final class ReaderTokenizer extends AbstractTokenizer
{
    final LinkedList<Character> qu = new LinkedList();
    final Reader reader;

    public ReaderTokenizer(Reader reader)
    {
	if (reader == null)
	    throw new NullPointerException("reader may not be null");
	this.reader = reader;
    }
    
    @Override public char getCh()
    {
	if (qu.isEmpty())
	    throw new RuntimeException("Trying to get a char with the empty queue");
	final Character ch = qu.pollFirst();
	return ch.charValue();
    }

    @Override public boolean hasCh()
    {
	if (!qu.isEmpty())
	    return true;
	final int ch;
	try {
	    ch = reader.read();
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
	if (ch < 0)
	    return false;
	qu.add(Character.valueOf((char)ch));
	return true;
    }

    @Override public void backCh(char ch)
    {
	qu.addFirst(Character.valueOf(ch));
    }

    static public Token[] tokenize(String text)
    {
	NullCheck.notNull(text, "text");
	final ReaderTokenizer t = new ReaderTokenizer(new StringReader(text));
	t.tokenize();
	return t.getOutput();
    }
}
