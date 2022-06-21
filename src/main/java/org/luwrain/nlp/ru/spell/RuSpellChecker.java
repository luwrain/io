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
    final Hunspell hunspell;

    public RuSpellChecker(Luwrain luwrain)
    {
	this.langTool = new JLanguageTool(new Russian());
	final File
	hunspellDataDir = new File(luwrain.getFileProperty(Luwrain.PROP_DIR_DATA), "hunspell"),
	dictFile = new File(hunspellDataDir, "ru_RU.dic"),
	affFile = new File(hunspellDataDir, "ru_RU.aff");
	this.hunspell = new Hunspell(dictFile.getAbsolutePath(), affFile.getAbsolutePath());
    }

    //Just for unit tests
RuSpellChecker()
    {
		this.langTool = new JLanguageTool(new Russian());
		this.hunspell = null;
    }

    @Override public List<SpellProblem> check(String text)
    {
	final List<SpellProblem> res = new ArrayList<>();
	try {
	    final List<RuleMatch> m = langTool.check(text);
	    for(RuleMatch  mm: m)
		res.add(new Problem(mm));
	    return res;
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    @Override public List<String> suggestCorrections(String word)
    {
	if (hunspell == null)
	    throw new IllegalStateException("Hunspell not initialized");
return hunspell.suggest(word);
    }

    
}
