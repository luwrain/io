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

//https://languagetool.org/development/api/org/languagetool/rules/spelling/SpellingCheckRule.html


import java.util.*;

import org.junit.*;

import org.luwrain.core.*;
import org.luwrain.nlp.*;

public class RuSpellCheckerTest extends Assert
{
    private RuSpellChecker checker = null;

        @Test public void shortSent()
    {
	final String text = "Это было превосходная день!";
	final SpellText t = new SpellText(new String[]{text}, checker);
	assertNotNull(t.getFragments());
	assertEquals(1, t.getFragments().size());
	assertNotNull(t.getProblems());
	assertEquals(1, t.getProblems().size());
		final List<List<LineMarks.Mark>> res = t.buildMarks();
		assertNotNull(res);
		assertEquals(1, res.size());
		List<LineMarks.Mark> marks = res.get(0);
		assertNotNull(marks);
		assertEquals(1, marks.size());
		final LineMarks.Mark mark = marks.get(0);
		assertNotNull(mark);
		final SpellProblem problem = (SpellProblem)mark.getMarkObject();
		assertNotNull(problem);
		assertEquals("Прилагательное не согласуется с существительным по роду.", problem.getComment());
		assertEquals("", problem.getShortComment());
		final int
		start = problem.getStart(),
		end = problem.getEnd();
		assertEquals("превосходная день", text.substring(start, end));
    }



    @Before public void  create()
    {
	checker = new RuSpellChecker();
    }
}
