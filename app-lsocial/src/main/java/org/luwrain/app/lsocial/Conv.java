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

package org.luwrain.app.lsocial;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.io.api.lsocial.publication.Section;

import static org.luwrain.popups.Popups.*;

final class Conv
{
    enum NewItemType {PUBL, PRES};

    private final Luwrain luwrain;
    private final Strings strings;

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    NewItemType newMainListItemType()
    {
	return (NewItemType)fixedList(luwrain, strings.newMainListItemTypePopupName(), new Object[]{NewItemType.PUBL, NewItemType.PRES});
    }

    int newPublSectType()
    {
	final String res = (String)fixedList(luwrain, strings.newPublSectTypePopupName(), new String[]{
		strings.typeMarkdown(),
		strings.typeLatex(),
		strings.typeMetapost(),
		strings.typeGnuplot(),
		strings.typeListing() });
	if (res == null)
	    return -1;
	if (res.equals(strings.typeMarkdown())) return Section.TYPE_MARKDOWN;
	if (res.equals(strings.typeLatex())) return Section.TYPE_LATEX;
	if (res.equals(strings.typeMetapost())) return Section.TYPE_METAPOST;
	if (res.equals(strings.typeGnuplot())) return Section.TYPE_GNUPLOT;
	if (res.equals(strings.typeListing())) return Section.TYPE_LISTING;
	return -1;
    }
}
