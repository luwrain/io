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

import org.languagetool.rules.*;

import org.luwrain.nlp.*;

final class Problem implements SpellProblem
{
private final String message, shortMessage;

    Problem(RuleMatch match)
    {
	this.message = match.getMessage();
	this.shortMessage = match.getShortMessage();
	/*
					List<SuggestedReplacement> repl = r.getSuggestedReplacementObjects();
				for(SuggestedReplacement rr: repl)
				    System.out.println(rr);
	*/
    }

    @Override public String getComment() { return message; }
    @Override public String getShortComment(){ return shortMessage; }
    @Override public int getStart(){ return 0; }
    @Override public int getEnd(){ return 0;}

	@Override public String toString()
    {
	return message;
    }
}
