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

package org.luwrain.antlr.latex;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.*;

import org.luwrain.core.*;

public class LatexTest extends Assert
{
    @Test public void twoPlusTwo() throws Exception
    {
	final String text = "2 + 2";
	final LatexLexer l = new LatexLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final LatexParser p = new LatexParser(tokens);
	final ParseTree tree = p.math();
	assertNotNull(tree);
	final ParseTreeWalker walker = new ParseTreeWalker();
	final List<LatexParser.MathUnitContext> units = new LinkedList();
	final EmptyLatexListener listener = new EmptyLatexListener(){
		@Override public void enterMathUnit(LatexParser.MathUnitContext c)
		{
		    units.add(c);
		}
	    };
	walker.walk(listener, tree);
	assertEquals(units.size(), 3);
	assertNotNull(units.get(0).Num());
	assertEquals(units.get(0).Num().toString(), "2");
	assertNotNull(units.get(1).MathOp());
	assertEquals(units.get(1).MathOp().toString(), "+");
	assertNotNull(units.get(2).Num());
	assertEquals(units.get(2).Num().toString(), "2");
    }

    @Test public void frac2and3() throws Exception
    {
	final String text = "\\frac {2} {3}";
	final LatexLexer l = new LatexLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final LatexParser p = new LatexParser(tokens);
	final ParseTree tree = p.math();
	assertNotNull(tree);
	final ParseTreeWalker walker = new ParseTreeWalker();
	final List<LatexParser.MathUnitContext> units = new LinkedList();
	final EmptyLatexListener listener = new EmptyLatexListener(){
		@Override public void enterMathUnit(LatexParser.MathUnitContext c)
		{
		    units.add(c);
		}
	    };
	walker.walk(listener, tree);
	assertEquals(units.size(), 5);
	
	assertNotNull(units.get(0).command());
	assertNotNull(units.get(0).command().Ident());
	assertEquals(units.get(0).command().Ident().toString(), "frac");
	assertNotNull(units.get(1).block());
	assertNotNull(units.get(1).block().mathUnit());
	assertNotNull(units.get(2).Num());
	assertEquals(units.get(2).Num().toString(), "2");
	assertNotNull(units.get(3).block());
	assertNotNull(units.get(3).block().mathUnit());
	assertNotNull(units.get(4).Num());
	assertEquals(units.get(4).Num().toString(), "3");
    }
}
