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
    private final int fromPos, toPos;

    Problem(RuleMatch match)
    {
	if (match.getMessage() != null)
	    this.message = match.getMessage().replaceAll("<suggestion>.*</suggestion>", ""); else
	    this.message = "";
	if (match.getShortMessage() != null)
	    this.shortMessage = match.getShortMessage().replaceAll("<suggestion>.*</suggestion>", ""); else
	    this.shortMessage = "";
	this.fromPos = match.getFromPos();
	this.toPos = match.getToPos();
	/*
	  List<SuggestedReplacement> repl = r.getSuggestedReplacementObjects();
	  for(SuggestedReplacement rr: repl)
	  rr;
	*/
    }

    @Override public String getComment() { return message; }
    @Override public String getShortComment(){ return shortMessage; }
    @Override public int getStart(){ return fromPos; }
    @Override public int getEnd(){ return toPos;}

	@Override public String toString()
    {
	return message;
    }
}
