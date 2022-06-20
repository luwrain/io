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

public class EditSpellChecking implements EditArea.ChangeListener
{
static private final String
    LOG_COMPONENT = "spelling";

    private final Luwrain luwrain;
    private final SpellChecker checker;
    public EditSpellChecking(Luwrain luwrain , String lang)
    {
	NullCheck.notNull(luwrain, "luwrain ");
	NullCheck.notEmpty(lang, "lang");
	this .luwrain = luwrain;
	this.checker = new SpellCheckerFactory().newChecker(luwrain, lang);
}

        public EditSpellChecking(Luwrain luwrain )
    {
	this(luwrain, "ru");
    }

    @Override public void onEditChange(EditArea editArea, MarkedLines lines, HotPoint hotPoint)
    {
	final SortedMap<Integer, String> text = new TreeMap<>();
	blockBounds(editArea, hotPoint.getHotPointY(),(line, marks)->(!line.trim().isEmpty()),
		    (lines_, index)->text.put(index, lines.getLine(index)));
	luwrain.executeBkg(()->check(editArea, text));
    }

public void initialChecking(EditArea editArea)
    {
	final SortedMap<Integer, String> text = new TreeMap<>();
	final Lines lines = editArea.getContent();
	for(int i = 0;i < lines.getLineCount();i++)
	    text.put(Integer.valueOf(i), lines.getLine(i));
	luwrain.executeBkg(()->check(editArea, text));
    }

    public void eraseSpellingMarks(EditArea editArea)
    {editArea.update((lines, hotPoint)->{
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


    private void check(EditArea editArea, SortedMap<Integer, String> text)
{
    //    Log.debug(LOG_COMPONENT, "Checking lines: " + text.size());
	final List<String> textLines = new ArrayList<>();
	for(Map.Entry<Integer, String> e: text.entrySet())
	    textLines.add(e.getValue());
	final SpellText spellText = new SpellText(textLines.toArray(new String[textLines.size()]), checker);
	final List<List<LineMarks.Mark>> marks = spellText.buildMarks();
	luwrain.runUiSafely(()->setResult(editArea, text, marks));
}

    private void setResult(EditArea editArea, SortedMap<Integer, String> text, List<List<LineMarks.Mark>> marks)
    {
	editArea.update((lines, hotPoint)->{
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
}
