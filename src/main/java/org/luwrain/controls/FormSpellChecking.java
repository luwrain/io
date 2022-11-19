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

package org.luwrain.controls;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.nlp.*;
import static org.luwrain.controls.EditUtils.*;

public class FormSpellChecking implements FormArea.MultilineEditChangeListener
{
static private final String
    LOG_COMPONENT = "spelling";

    private final Luwrain luwrain;
    private final SpellChecker checker;
    public FormSpellChecking(Luwrain luwrain , String lang)
    {
	NullCheck.notNull(luwrain, "luwrain ");
	NullCheck.notEmpty(lang, "lang");
	this .luwrain = luwrain;
	this.checker = new SpellCheckerFactory().newChecker(luwrain, lang);
}

        public FormSpellChecking(Luwrain luwrain )
    {
	this(luwrain, "ru");
    }

    @Override public void onEditChange(FormArea formArea, Event event, MarkedLines lines, HotPoint hotPoint)
    {
	final SortedMap<Integer, String> text = new TreeMap<>();
	blockBounds(formArea, hotPoint.getHotPointY(),(line, marks)->(!line.trim().isEmpty()),
		    (lines_, index)->text.put(index, lines.getLine(index)));
	luwrain.executeBkg(()->check(formArea, text));
    }

public void initialChecking(FormArea formArea)
    {
	final SortedMap<Integer, String> text = new TreeMap<>();
	final Lines lines = formArea.getMultilineEditContent();
	for(int i = 0;i < lines.getLineCount();i++)
	    text.put(Integer.valueOf(i), lines.getLine(i));
	luwrain.executeBkg(()->check(formArea, text));
    }

    public void eraseSpellingMarks(FormArea formArea)
    {
	formArea.updateMultilineEdit((lines, hotPoint)->{
		for(int i = 0;i < lines.getLineCount();i++)
		{
		    final LineMarks marks = lines.getLineMarks(i);
		    if (marks == null)
			continue;
		    lines.setLineMarks(i, marks.filter((mark)->{
				return mark.getMarkObject() == null || !(mark.getMarkObject() instanceof SpellProblem);
			    }));
		}
		return false;
	    });
    }

    private void check(FormArea formArea, SortedMap<Integer, String> text)
{
    //    Log.debug(LOG_COMPONENT, "Checking lines: " + text.size());
	final List<String> textLines = new ArrayList<>();
	for(Map.Entry<Integer, String> e: text.entrySet())
	    textLines.add(e.getValue());
	final SpellText spellText = new SpellText(textLines.toArray(new String[textLines.size()]), checker);
	final List<List<LineMarks.Mark>> marks = spellText.buildMarks();
	luwrain.runUiSafely(()->setResult(formArea, text, marks));
}

    private void setResult(FormArea formArea, SortedMap<Integer, String> text, List<List<LineMarks.Mark>> marks)
    {
	formArea.updateMultilineEdit((lines, hotPoint)->{
		int index = 0;
		for(Map.Entry<Integer, String> e: text.entrySet())
		{
		    final int lineIndex = e.getKey().intValue();
		    lines.setLineMarks(lineIndex, new DefaultLineMarks.Builder(lines.getLineMarks(lineIndex)).addAll(marks.get(index)).build());

		    Log.debug(LOG_COMPONENT, "Result for line " + lineIndex + ", " + String.valueOf(marks.get(index).size()) + " marks");
		    for(LineMarks.Mark m: marks.get(index))
			Log.debug(LOG_COMPONENT, m.getMarkObject().toString());
					    index++;
		}
		return false;
	    });
    }

    public SpellChecker getSpellChecker()
    {
	return checker;
    }
}
