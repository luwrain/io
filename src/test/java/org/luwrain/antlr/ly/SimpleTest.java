
package org.luwrain.antlr.ly;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.junit.*;

import org.luwrain.core.*;

public class SimpleTest extends Assert
{
    @Test public void version() throws Exception
    {
	final String text = "\\version \"2.14.2\"";
	final LilypondLexer l = new LilypondLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
final LilypondParser p = new LilypondParser(tokens);
final ParseTree tree = p.score();
assertNotNull(tree);
final ParseTreeWalker walker = new ParseTreeWalker();
final TestListener listener = new TestListener();
walker.walk(listener, tree);
	    }
    }
