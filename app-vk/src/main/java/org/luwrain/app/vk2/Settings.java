/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.vk2;

import org.luwrain.core.*;

interface Settings
{
    static final String PATH = "/org/luwrain/app/vk";

    int getUserId(int defValue);
    String getAccessToken(String defValue);

    static Settings create(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	return null;//FIXME:newreg RegistryProxy.create(luwrain.getRegistry(), PATH, Settings.class);
    }
}
