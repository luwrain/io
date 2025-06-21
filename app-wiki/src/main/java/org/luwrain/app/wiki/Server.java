/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.wiki;

import java.lang.reflect.*;
import java.util.*;

import com.google.gson.annotations.*;
import com.google.gson.reflect.*;

final class Server
{
    static final Type LIST_TYPE = new TypeToken<List<Server>>(){}.getType();

    @SerializedName("name")
    String name = null;

    @SerializedName("searchUrl")
    String searchUrl = null;

    @SerializedName("pagesUrl")
    String pagesUrl = null;

    @Override public String toString()
    {
	return this.name != null?this.name:"";
    }
}
