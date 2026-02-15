// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.ai;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

}
