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
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.io.api.lsocial.publication.Publication;
import org.luwrain.io.api.lsocial.publication.Section;
import org.luwrain.app.lsocial.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;
import static org.luwrain.app.base.LayoutBase.*;

class PublSectLayoutExt extends PublLayoutExt
{
    static private final Logger log = LogManager.getLogger();

PublSectLayoutExt(MainLayout mainLayout, Publication publ)
    {
	super(mainLayout, publ);

	/*
	actions = mainLayout.actions(
			  mainLayout.action("insert", s.create(), new InputEvent(Special.INSERT), this::onInsert)
);
	*/
    }

    @Override public void setLayout()
    {
	mainLayout.setAreaLayout(AreaLayout.LEFT_RIGHT, mainLayout.mainList, mainLayout.mainListActions,
				 sectList, actions);
    }

    @Override public void activateDefaultArea()
    {
	mainLayout.setActiveArea(sectList);
    }
}
