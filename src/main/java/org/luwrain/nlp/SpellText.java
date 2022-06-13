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

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.DefaultLineMarks.*;
import static org.luwrain.util.RangeUtils.*;

public class SpellText
{
static public final String
    LOG_COMPONENT = "spelling";

    final String text;
    final List<Fragment> fragments = new ArrayList<>();
    final List<SpellProblem> problems;

    public SpellText(String[] text, SpellChecker checker)
    {
	if (text.length == 0)
	{
	    this.text = "";
	    this.problems = Arrays.asList();
	    return;
	}
	if (text.length == 1)
	{
	    this.text = text[0];
	    fragments.add(new Fragment(0, text[0].length()));
	    Log.debug(LOG_COMPONENT, "Checking '" + this.text + "'");
	    	this.problems = checker.check(this.text);
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
		    Log.debug(LOG_COMPONENT, "Checking '" + this.text + "'");
	this.problems = checker.check(this.text);
    }

    public List<List<LineMarks.Mark>> buildMarks()
    {
	final List<List<LineMarks.Mark>> res = new ArrayList<>();
	for(Fragment f: fragments)
	{
	    final List<LineMarks.Mark> a = new ArrayList();
	    for(SpellProblem p: problems)
	    {
		final int[] range = commonRangeByBounds(p.getStart(), p.getEnd(), f.posFrom, f.posTo);
		if (range == null)
		    continue;
		a.add(new MarkImpl(LineMarks.Mark.Type.WEAK, range[0] - f.posFrom, range[1] - f.posFrom, p));
	}
	    res.add(a);
    }
	return res;
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
