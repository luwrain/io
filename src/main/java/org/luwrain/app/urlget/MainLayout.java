/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.urlget;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase
{
    private final App app;
    final NavigationArea area;

    MainLayout(App app)
    {
	super(app);
	this.app = app;

	this.area = new NavigationArea(getControlContext()){
		@Override public int getLineCount() { return app.text.length > 0?app.text.length:1; }
		@Override public String getLine(int index) { return app.text[index]; }
		@Override public String getAreaName() { return app.getStrings().appName(); }
	    };
    setAreaLayout(area, null);
    }
}
