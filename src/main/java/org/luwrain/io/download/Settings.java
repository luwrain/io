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

package org.luwrain.io.download;

import java.util.*;

import org.luwrain.core.*;

public interface Settings
{
    public static final String COMPLETED = "completed";
    public static final String FAILED = "failed";

    static public final String DOWNLOAD_PATH = "/org/luwrain/io/download";

    public interface Entry
    {
	String getUrl(String defValue);
	void setUrl(String value);
	String getDestFile(String defValue);
	void setDestFile(String value);
	String getStatus(String defValue);
	void setStatus(String value);
	String getErrorInfo(String defValue);
	void setErrorInfo(String value);
    }

    static public Entry createEntry(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	if (id < 0)
	    throw new IllegalArgumentException("id may not be negative (" + id + ")");
	final String path = Registry.join(DOWNLOAD_PATH, "" + id);
	registry.addDirectory(path);
	return RegistryProxy.create(registry, path, Entry.class);
    }

    static public int addEntry(Registry registry, String url, String destFile)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notEmpty(url, "url");
	NullCheck.notEmpty(destFile, "destFile");
	registry.addDirectory(DOWNLOAD_PATH);
	final int id = Registry.nextFreeNum(registry, DOWNLOAD_PATH);
	final Entry entry = createEntry(registry, id);
	entry.setUrl(url);
	entry.setDestFile(destFile);
	return id;
    }

    static public int[] getIds(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	registry.addDirectory(DOWNLOAD_PATH);
	final String[] dirs = registry.getDirectories(DOWNLOAD_PATH);
	final List<Integer> res = new Vector();
	for(String s: dirs)
	{
	    try {
		res.add(new Integer(Integer.parseInt(s)));
	    }
	    catch(NumberFormatException e)
	    {
	    }
	}
	final int[] ints = new int[res.size()];
	for(int i = 0;i < ints.length;++i)
	    ints[i] = res.get(i).intValue();
	return ints;
    }
}
