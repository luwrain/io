/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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


package org.luwrain.popups;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.io.*;

public class WebSearchResultPopup extends ListPopupBase
{
    protected final WebSearchResult webSearchResult;

    
    public WebSearchResultPopup(Luwrain luwrain, String name, WebSearchResult webSearchResult, Set<Popup.Flags> popupFlags)
    {
	super(luwrain, createParams(luwrain, name), popupFlags);
	this.webSearchResult = webSearchResult;
    }

    static protected ListArea.Params createParams(Luwrain luwrain, String name)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(name, "name");
	final ListArea.Params params = new ListArea.Params();
	params.context = new DefaultControlContext(luwrain);
	params.name = name;
	params.model = new ListUtils.FixedModel();
	return params;
    }
}
