// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

import static org.luwrain.popups.Popups.*;

final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    String prompt(String title, String prefix, String initial)
    {
	final var res = textNotEmpty(luwrain,
				     title,
				     prefix,
				     initial);
	if (res == null)
	    return null;
	return res.trim();
    }
}
