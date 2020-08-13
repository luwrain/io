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

package org.luwrain.nlp.ru;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.luwrain.antlr.rutoken.*;

public class TokenFilterBuilder
{
    public TokenFilter build(String text)
    {
	final TokenFilterNotationLexer l = new TokenFilterNotationLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final TokenFilterNotationParser p = new TokenFilterNotationParser(tokens);
	final ParseTree tree = p.notation();
	final ParseTreeWalker walker = new ParseTreeWalker();
	final List<Token[]> resTokens = new LinkedList();
	final List<Boolean> resOptional = new LinkedList();
	final TokenFilterNotationListener listener = new EmptyTokenFilterNotationListener(){
		private List<Token> tokens = new LinkedList();
		@Override public void exitUnit(TokenFilterNotationParser.UnitContext c)
		{
		    resOptional.add(c.getChildCount() == 2?true:false);
		    resTokens.add(tokens.toArray(new Token[tokens.size()]));
		    tokens.clear();
		}
				@Override public void enterUnitBase(TokenFilterNotationParser.UnitBaseContext c)
		{
		    if (c.Cyril() != null)
		    {
			this.tokens.add(new Token(Token.Type.CYRIL, c.Cyril().toString().toUpperCase()));
			return;
		    }
		    		    if (c.Latin() != null)
		    {
			this.tokens.add(new Token(Token.Type.LATIN, c.Latin().toString()));
			return;
		    }
				    		    		    if (c.Space() != null)
		    {
			this.tokens.add(new Token(Token.Type.SPACE, c.Space().toString()));
			return;
		    }
								    				    		    		    if (c.Num() != null)
		    {
			this.tokens.add(new Token(Token.Type.NUM, c.Num().toString()));
			return;
		    }
																    								    				    		    		    if (c.Punc() != null)
		    {
			this.tokens.add(new Token(Token.Type.PUNC, c.Punc().toString()));
			return;
		    }
		}
	    };
	walker.walk(listener, tree);
	final Boolean[] optional = resOptional.toArray(new Boolean[resOptional.size()]);
	final boolean[] optionalBools = new boolean[optional.length];
	for(int i = 0;i < optional.length;i++)
	    optionalBools[i] = optional[i].booleanValue();
	return new TokenFilter(resTokens.toArray(new Token[resTokens.size()][]), optionalBools);
}
}
