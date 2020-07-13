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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.*;

import org.luwrain.core.*;

public class LatexTest extends Assert
{
    @Test public void twoPlusTwo() throws Exception
    {
	final String text = "2+2";
	final LatexLexer l = new LatexLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
final LatexParser p = new LatexParser(tokens);
final ParseTree tree = p.score();
assertNotNull(tree);
final ParseTreeWalker walker = new ParseTreeWalker();
final EmptyLatexListener listener = new EmptyLatexListener();
walker.walk(listener, tree);
    }
}
