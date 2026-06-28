// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.searx;

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
