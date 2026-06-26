// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

import static org.luwrain.popups.Popups.*;

public final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;
    private final Set<String> runHistory = new TreeSet<>();

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    public String newAccountName()
    {
	return textNotEmpty(luwrain, strings.newAccountNamePopupName(), strings.newAccountNamePopupPrefix(), "");
    }
}
