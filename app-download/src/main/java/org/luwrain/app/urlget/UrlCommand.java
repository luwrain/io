/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.urlget;

//import java.net.*;
//import java.util.*;
//import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;


public final class UrlCommand implements  Command
{
    @Override public String getName()
    {
	return "url";
    }

    @Override public void onCommand(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final String res = Popups.textNotEmpty(luwrain, "URL", "URL:", "");
	if (res == null || res.trim().isEmpty())
	    return;
    }
}
