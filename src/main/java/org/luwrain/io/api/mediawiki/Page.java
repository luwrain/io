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

package org.luwrain.io.api.mediawiki;

import org.luwrain.core.*;

public final class Page
{
    public final String baseUrl;
    public final String title;
    public final String comment;

    Page(String baseUrl, String title, String comment)
    {
	NullCheck.notNull(baseUrl, "baseUrl");
	NullCheck.notNull(title, "title");
	NullCheck.notNull(comment, "comment");
	this.baseUrl = baseUrl;
	this.title = title;
	this.comment = comment;
    }

    @Override public String toString()
    {
	if (comment.trim().isEmpty())
	    return title.trim();
	return title.trim() + ", " + comment.trim();
    }
}
