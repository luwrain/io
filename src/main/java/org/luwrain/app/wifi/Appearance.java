/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.wifi;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.network.*;

class Appearance implements ListItemAppearance
{
    private Luwrain luwrain;
    private Strings strings;

Appearance(Luwrain luwrain, Strings strings)
    {
	this.luwrain = luwrain;
	this.strings = strings;
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
    }

    @Override public void introduceItem(Object item, int flags)
    {
	NullCheck.notNull(item, "item");
	if (!(item instanceof WifiNetwork))
	    return;
	    final WifiNetwork network = (WifiNetwork)item;
	    luwrain.playSound(Sounds.NEW_LIST_ITEM);
	    if (network.hasPassword() && ((flags & ListItemAppearance.BRIEF) == 0))
	    luwrain.say("Защищённая сеть " + network.toString()); else
	    luwrain.say(network.toString());
    }

    @Override public String getScreenAppearance(Object item, int flags)
    {
	NullCheck.notNull(item, "item");
	return item.toString();
    }

    @Override public int getObservableLeftBound(Object item)
    {
	return 0;
    }

    @Override public int getObservableRightBound(Object item)
    {
	return getScreenAppearance(item, 0).length();
    }
}
