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

package org.luwrain.nlp;

import java.util.*;

public class SpellText
{
    final String text;
    private final List<Fragment> fragments = new ArrayList<>();
    private List<SpellProblem> problems;

    public SpellText(String[] text, SpellChecker checker)
    {
	if (text.length == 0)
	{
	    this.text = "";
	    return;
	}
	if (text.length == 1)
	{
	    this.text = text[0];
	    fragments.add(new Fragment(0, text[0].length()));
	    return;
	}
	final StringBuilder b = new StringBuilder();
	b.append(text[0]);
	fragments.add(new Fragment(0, b.length()));
	for(int i = 1;i < text.length;i++)
	{
	    fragments.add(new Fragment(b.length() + 1, b.length() + text[i].length() + 1));
	    b.append(" ").append(text[i]);
	}
	this.text = new String(b);
	if (fragments.size() != text.length)
	    throw new IllegalStateException("the fragments and text arrays have different length");
	this.problems = checker.check(this.text);
    }

    static public final class Fragment
    {
	final int posFrom, posTo;
	Fragment(int posFrom, int posTo)
	{
	    this.posFrom = posFrom;
	    this.posTo = posTo;
	}
    }
    }
