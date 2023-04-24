/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>

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
import org.luwrain.core.events.*;
import org.luwrain.nlp.*;
//import static org.luwrain.controls.EditUtils.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.NullCheck.*;

public class SpellCheckingEditArea extends EditArea
{
    protected     final EditSpellChecking spellChecking;
    
    public SpellCheckingEditArea(Luwrain luwrain, EditArea.Params params)
    {
	super(params);
	this.spellChecking = new EditSpellChecking(luwrain);
	this.changeListeners.add(spellChecking);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	notNull(event, "event");
	if (event.getType() != SystemEvent.Type.REGULAR)
	    return super.onSystemEvent(event);
	switch(event.getCode())
	{
	case IDLE:
	    return onIdle(event);
	default:
	    return super.onSystemEvent(event);
	}
    }

        protected  boolean onIdle(SystemEvent event)
    {
	final MarkedLines lines = getContent();
	final int
	x = getHotPointX(),
	y = getHotPointY();
	if (y >= lines.getLineCount())
	    return true;
	final LineMarks marks = lines.getLineMarks(y);
	if (marks == null)
	    return  true;
	final LineMarks.Mark[] atPoint = marks.findAtPos(x);
	if (atPoint == null || atPoint.length == 0)
	    return true;
	for(LineMarks.Mark m: atPoint)
	{
	    if (m.getMarkObject() == null || !(m.getMarkObject() instanceof SpellProblem))
		continue;
	    final SpellProblem p = (SpellProblem)m.getMarkObject();
	    context.message(p.getComment(), Luwrain.MessageType.ANNOUNCEMENT);
	    return true;
	}
	return true;
    }

    @Override public void announceLine(int index, String line)
    {
	final String text = getSpeakableText(line);
	if (!hasSpellProblems(index))
	    	    NavigationArea.defaultLineAnnouncement(context, index, text); else
	    context.setEventResponse(text(Sounds.SPELLING, text));
    }

    public String getSpeakableText(String text)
    {
	return context.getSpeakableText(text, Luwrain.SpeakableTextType.NATURAL);
    }

    public boolean hasSpellProblems(int lineIndex)
    {
	if (getContent().getLineMarks(lineIndex) == null)
	    return false;
	final LineMarks.Mark[] marks = getContent().getLineMarks(lineIndex).getMarks();
	if (marks == null)
	    return false;
	for(LineMarks.Mark m: marks)
	    if (m.getMarkObject() != null && m.getMarkObject() instanceof SpellProblem)
		return true;
	return false;
    }
}
