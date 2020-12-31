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

package org.luwrain.nlp.ru;

import java.util.*;
import java.util.function.*;

import org.luwrain.core.*;
import org.luwrain.nlp.ru.Token.Type;
import org.luwrain.nlp.RomanNum;

import org.luwrain.script.*;

public final class TokenPlaceholder
{
    final Set<String> cyrilValues = new HashSet();
    final Set<String> latinValues = new HashSet();
    final Set<String> puncValues = new HashSet();
    final boolean optional;

    TokenPlaceholder(Token[] tokens, boolean optional)
    {
	for(Token t: tokens)
	{
	    switch(t.type)
	    {
	    case CYRIL:
		cyrilValues.add(t.text);
		break;
	    }
	}
	this.optional = optional;
    }

    public boolean match(Token token)
    {
	switch(token.type)
	{
	case CYRIL:
	    return cyrilValues.contains(token.text.toUpperCase());
	}
	return false;
    }
}
