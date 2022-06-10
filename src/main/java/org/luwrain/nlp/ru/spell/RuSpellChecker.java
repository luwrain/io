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

package org.luwrain.nlp.ru.spell;

import java.util.*;
import java.io.*;

import org.languagetool.*;
import org.languagetool.language.*;
import org.languagetool.rules.*;
import org.languagetool.rules.spelling.*;

import org.luwrain.core.*;
import org.luwrain.nlp.*;

public final class RuSpellChecker implements SpellChecker
{
    final JLanguageTool langTool;

    public RuSpellChecker()
    {
	this.langTool = new JLanguageTool(new Russian());
    }

    @Override public List<SpellProblem> check(String text)
    {
	final List<SpellProblem> res = new ArrayList<>();
	try {
	    final List<RuleMatch> m = langTool.check(text);
	    System.out.println("match " + m.size());
	    for(RuleMatch  mm: m)
		res.add(new Problem(mm));
	    return res;
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }
}
