// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

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
