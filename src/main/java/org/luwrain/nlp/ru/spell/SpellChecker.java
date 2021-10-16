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

package org.luwrain.nlp.ru.spell;

import java.util.*;
import java.io.*;

import org.languagetool.*;
import org.languagetool.language.*;
import org.languagetool.rules.*;
import org.languagetool.rules.spelling.*;

import org.luwrain.core.*;


public final class SpellChecker
{
    private void setupSpellCheckTool()
    {
	final JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
	for (Rule rule : langTool.getAllActiveRules())
	{
	    if (rule instanceof SpellingCheckRule)
	    {
		final 	                SpellingCheckRule r = (SpellingCheckRule)rule;
		r.acceptPhrases(Arrays.asList());
	    }
	}
    }
}
