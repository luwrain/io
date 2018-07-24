/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.app.download;

import java.util.*;
import java.net.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

final class Actions
{
    private final Luwrain luwrain;
    private final Strings strings;
    private final Base base;

    Actions(Luwrain luwrain, Strings strings, Base base)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(base, "base");
	this.luwrain = luwrain;
	this.strings = strings;
	this.base = base;
    }

    boolean onClipboardPaste()
    {
	final Object[] objs = luwrain.getClipboard().get();
	for(Object obj: objs)
	{
	    if (obj instanceof URL)
	    {
		final URL url = (URL)obj;
		try {
		    base.manager.addDownload(url, suggestDestFile(url));
		}
		catch(IOException e)
		{
		    luwrain.message(strings.downloadAddingError(luwrain.i18n().getExceptionDescr(e)), Luwrain.MessageType.ERROR);
		    return true;
		}
		continue;
	    }
	    final URL url;
	    try {
		url = new URL(obj.toString());
	    }
	    catch(MalformedURLException e)
	    {
		luwrain.message(strings.unableToMakeUrl(obj.toString()), Luwrain.MessageType.ERROR);
		return true;
	    }
	    try {
		base.manager.addDownload(url, suggestDestFile(url));
	    }
	    catch(IOException e)
	    {
		luwrain.message(strings.downloadAddingError(luwrain.i18n().getExceptionDescr(e)), Luwrain.MessageType.ERROR);
		return true;
	    }
	}
	return true;
    }

    private File suggestDestFile(URL url)
    {
	NullCheck.notNull(url, "url");
	final File destDir = new File("/tmp");
	final String path = url.getFile();
	if (path == null || path.isEmpty())
	    return new File(destDir, simplify(url.toString()));
	final int lastSlash = path.lastIndexOf("/");
	final int lastBackslash = path.lastIndexOf("\\");
	final int pos = Math.max(lastSlash, lastBackslash);
	if (pos < 0)
	    return new File(destDir, path);
	if (pos + 1 >= path.length())
	    return new File(destDir, simplify(url.toString()));
	return new File(destDir, path.substring(pos + 1));
    }

    private String simplify(String str)
    {
	NullCheck.notNull(str, "str");
	final StringBuilder b = new StringBuilder();
	boolean wasNonDash = false;
	for(int i = 0;i < str.length();++i)
	{
	    final char c = str.charAt(i);
	    if (Character.isLetter(c) || Character.isDigit(c))
	    {
		b.append("" + c);
		wasNonDash = true;
		continue;
	    }
	    if (wasNonDash)
		b.append("-");
	    wasNonDash = false;
	}
	final String res = new String(b);
	return !res.isEmpty()?res:"-";
    }
}
