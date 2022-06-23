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
import java.lang.reflect.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.DefaultLineMarks.*;
import static org.luwrain.util.RangeUtils.*;

public class SpellExclusion
{
    static private final String
	NLP_DIR = "luwrain.nlp",
	EXCLUSION_FILE = "spelling-exclusion.json";
    static final Type
	EXCLUSION_LIST_TYPE = new TypeToken<List<Exclusion>>(){}.getType();

    private final Luwrain luwrain;
    private final List<Exclusion> exclusions = new ArrayList<>();
    public SpellExclusion(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    public List<Exclusion> getExclusions()
    {
	return this.exclusions;
    }

    public void load()
    {
	final Gson gson = new Gson();
	try {
	    final List<Exclusion> res; 
	    try(final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), "UTF-8"))) {
		res = gson.fromJson(r, EXCLUSION_LIST_TYPE);
	    }
	    exclusions.clear();
	    if (res != null)
		exclusions.addAll(res);
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    public void save()
    {
	final Gson gson = new Gson();
	try {
	    try(final BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile()), "UTF-8"))) {
		gson.toJson(exclusions, w);
		w.flush();
	    }
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    private File getFile()
    {
	return new File(luwrain.getAppDataDir(NLP_DIR).toFile(), EXCLUSION_FILE);
    }

    static public final class Exclusion
    {
	String type = null, text = null;
	public String getType() { return type != null?type:""; }
	public void setType(String type) { this.type = type; }
	public String getText() { return text != null?text:""; }
	public void setText(String text) { this.text = text; }
    }
}
