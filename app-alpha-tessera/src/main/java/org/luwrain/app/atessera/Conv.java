// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.lsocial;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import alpha4.json.*;
import alpha4.json.Publication.Section;

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

    Publication.SectionType newPublSectType()
    {
	final String res = (String)fixedList(luwrain, strings.newPublSectTypePopupName(), new String[]{
		strings.typeMarkdown(),
		strings.typeLatex(),
		strings.typeMetapost(),
		strings.typeGnuplot(),
		strings.typeListing() });
	if (res == null)
	    return null;
	if (res.equals(strings.typeMarkdown())) return Publication.SectionType.MARKDOWN;
	if (res.equals(strings.typeLatex())) return Publication.SectionType.LATEX;
	if (res.equals(strings.typeMetapost())) return Publication.SectionType.METAPOST;
	if (res.equals(strings.typeGnuplot())) return Publication.SectionType.GNUPLOT;
	if (res.equals(strings.typeListing())) return Publication.SectionType.LISTING;
	return null;
    }
}
