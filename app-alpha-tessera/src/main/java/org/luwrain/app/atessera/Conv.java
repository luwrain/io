// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.app.atessera.Publication.Section;
import alpha4.PublicationSectionType;

import static org.luwrain.popups.Popups.*;

final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;
    private String prompt = "";

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

        String prompt()
    {
final var res = textNotEmpty(luwrain,
			    strings.promptPopupName(),
			    strings.promptPopupPrefix(),
			    prompt);
if (res == null)
    return null;
prompt = res;
return res.trim();
    }


    PublicationSectionType newPublSectType()
    {
	final String res = (String)fixedList(luwrain, strings.newPublSectTypePopupName(), new String[]{
		strings.typeMarkdown(),
		strings.typeLatex(),
		strings.typeMetapost(),
		strings.typeGnuplot(),
		strings.typeListing() });
	if (res == null)
	    return null;
	if (res.equals(strings.typeMarkdown())) return PublicationSectionType.MARKDOWN;
	if (res.equals(strings.typeLatex())) return PublicationSectionType.LATEX;
	if (res.equals(strings.typeMetapost())) return PublicationSectionType.METAPOST;
	if (res.equals(strings.typeGnuplot())) return PublicationSectionType.GNUPLOT;
	if (res.equals(strings.typeListing())) return PublicationSectionType.LISTING;
	return null;
    }
}
