/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.antlr.ly;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.junit.*;

import org.luwrain.core.*;

public class LilypondTest extends Assert
{
    @Test public void version() throws Exception
    {
	final String text = "\\version \" 2.14.2 \"";
	final LilypondLexer l = new LilypondLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final LilypondParser p = new LilypondParser(tokens);
	final ParseTree tree = p.music();
	assertNotNull(tree);
	final ParseTreeWalker walker = new ParseTreeWalker();
	final List<LilypondParser.MusicUnitContext> units = new ArrayList();
	final TestListener listener = new TestListener(){
		@Override public void enterMusicUnit(LilypondParser.MusicUnitContext c)
		{
		    units.add(c);
		}
	    };
	walker.walk(listener, tree);
	assertEquals(units.size(), 2);
	assertNotNull(units.get(0).command());
	assertNotNull(units.get(0).command().Ident());
	assertEquals(units.get(0).command().Ident().toString(), "version");
	assertNotNull(units.get(1).String());
	assertEquals(units.get(1).String().toString(), "\" 2.14.2 \"");
    }

    @Test public void note() throws Exception
    {
	final String text = "a,1";
	final LilypondLexer l = new LilypondLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final LilypondParser p = new LilypondParser(tokens);
	final ParseTree tree = p.music();
	assertNotNull(tree);
	final ParseTreeWalker walker = new ParseTreeWalker();
	final List<LilypondParser.MusicUnitContext> units = new ArrayList();
	final TestListener listener = new TestListener(){
		@Override public void enterMusicUnit(LilypondParser.MusicUnitContext c)
		{
		    units.add(c);
		}
	    };
	walker.walk(listener, tree);
	assertEquals(units.size(), 1);
	assertNotNull(units.get(0).note());
	assertNotNull(units.get(0).note().NoteName());
	assertNotNull(units.get(0).note().Oct());
	assertNotNull(units.get(0).note().Duration());
	assertEquals(units.get(0).note().NoteName().toString(), "a");
	assertEquals(units.get(0).note().Oct().toString(), ",");
	assertEquals(units.get(0).note().Duration().toString(), "1");
    }

    @Test public void doubleNote() throws Exception
    {
	final String text = "a1a1";
	final LilypondLexer l = new LilypondLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final LilypondParser p = new LilypondParser(tokens);
	final ParseTree tree = p.music();
	assertNotNull(tree);
	final ParseTreeWalker walker = new ParseTreeWalker();
	final List<LilypondParser.MusicUnitContext> units = new ArrayList();
	final TestListener listener = new TestListener(){
		@Override public void enterMusicUnit(LilypondParser.MusicUnitContext c)
		{
		    units.add(c);
		}
	    };
	walker.walk(listener, tree);
	assertEquals(units.size(), 2);
	for(LilypondParser.MusicUnitContext u: units)
	{
	    assertNotNull(u.note().NoteName());
	    assertEquals(u.note().NoteName().toString(), "a");
	    assertNotNull(u.note().Duration());
	    assertEquals(u.note().Duration().toString(), "1");
	}
    }
}
