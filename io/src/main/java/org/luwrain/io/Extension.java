// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io;

import java.util.*;
import com.google.auto.service.*;
import org.luwrain.core.*;

import static java.util.Objects.*;

@AutoService(org.luwrain.core.Extension.class)
public final class Extension extends EmptyExtension
{
    private org.luwrain.io.download.Manager download;

    @Override public String init(Luwrain luwrain)
    {
	requireNonNull(luwrain, "luwrain can't be null");
	download = new org.luwrain.io.download.Manager(luwrain);
	return null;
    }

    @Override public void close()
    {
	download.close();
	download = null;
    }
    
    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{
	    new WebCommand()
	};
    }
}
