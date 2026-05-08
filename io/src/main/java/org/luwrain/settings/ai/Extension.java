// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.settings.ai;

import com.google.auto.service.*;

import org.luwrain.core.*;

@AutoService(org.luwrain.core.Extension.class)
public final class Extension extends EmptyExtension
{
    @Override public org.luwrain.cpanel.Factory[] getControlPanelFactories(Luwrain luwrain)
    {
	return new org.luwrain.cpanel.Factory[]{
	    new Factory(luwrain),
	};
    }
}
