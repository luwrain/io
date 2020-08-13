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

public final class TokenFilters
{
    static private final Map<String, TokenFilter> filters = new HashMap();
    static private final TokenFilterBuilder builder = new TokenFilterBuilder();

    static public TokenFilter get(String exp)
    {
	if (exp.isEmpty())
	    return null;
	TokenFilter f = filters.get(exp);
	if (f != null)
	    return f;
	f = builder.build(exp);
	filters.put(exp, f);
	return f;
    }
}
