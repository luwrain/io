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

package org.luwrain.nlp;

import java.util.*;
import java.io.*;
import java.net.*;

public final class RomanNum
{
    static private final String RESOURCE_PATH = "org/luwrain/grammar/romannum.txt";

    private final String[] values;

    public RomanNum()
    {
	final URL url = this.getClass().getClassLoader().getResource(RESOURCE_PATH);
	if (url == null)
	    throw new RuntimeException("No resource " + RESOURCE_PATH);
	final List<String> res = new ArrayList<>();
	res.add("");//for zero
	try {
	    final BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
	    try {
		String line = r.readLine();
		while(line != null)
		{
		    if (!line.trim().isEmpty())
			res.add(line.trim());
		    line = r.readLine();
		}
	    }
	    finally {
		r.close();
	    }
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
	    this.values = res.toArray(new String[res.size()]);
    }

	public int find(String s)
	{
	    if (s == null)
		throw new NullPointerException("s can't be null");
	    if (s.isEmpty())
		throw new IllegalArgumentException("s can't be empty");
	    for(int i = 0;i < values.length;i++)
		if (values[i].equals(s))
		    return i;
	    return -1;
	}

    public String get(int num)
    {
	if (num < 1 || num >= values.length)
	    throw new IllegalArgumentException("num (" + String.valueOf(num) + ") must be greater than zero and less than " + String.valueOf(values.length));
	return values[num];
    }

    public String[] getAll()
    {
	return values.clone();
    }
}
