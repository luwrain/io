/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.settings.yandex;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

public final class Factory implements org.luwrain.cpanel.Factory
{
    final Luwrain luwrain;
    final DefaultElement el = new DefaultElement(StandardElements.APPLICATIONS, Factory.class.getName());
    private Strings strings = null;

    Factory(Luwrain luwrain)
    {
	this.luwrain = luwrain;
    }

    @Override public Element[] getElements()
    {
	if (strings == null)
	    strings = luwrain.i18n().getStrings(Strings.class);
	return new Element[] {el};
    }

    @Override public Section createSection(Element el)
    {
		if (strings == null)
	    strings = luwrain.i18n().getStrings(Strings.class);
		return new DefaultSection(el, strings.name(), c -> new ParamsArea(c));
    }

        @Override public Element[] getOnDemandElements(Element parent)
    {
	return new Element[0];
    }
}
