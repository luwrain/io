
package org.luwrain.antlr.rutoken;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class EmptyTokenFilterNotationListener implements TokenFilterNotationListener
{
    @Override public void enterNotation(TokenFilterNotationParser.NotationContext ctx) {}
    @Override public void exitNotation(TokenFilterNotationParser.NotationContext ctx) {}

        @Override public void enterAlter(TokenFilterNotationParser.AlterContext ctx) {}
    @Override public void exitAlter(TokenFilterNotationParser.AlterContext ctx) {}




    @Override public void enterUnit(TokenFilterNotationParser.UnitContext ctx) {}
    @Override public void exitUnit(TokenFilterNotationParser.UnitContext ctx) {}

        @Override public void enterUnitBase(TokenFilterNotationParser.UnitBaseContext ctx) {}
    @Override public void exitUnitBase(TokenFilterNotationParser.UnitBaseContext ctx) {}


    @Override public void enterEveryRule(ParserRuleContext ctx) {}
    @Override public void exitEveryRule(ParserRuleContext ctx) {}
    @Override public void visitTerminal(TerminalNode node) {}

        @Override public void visitErrorNode(ErrorNode node)
    {
	throw new IllegalStateException(node.toString());
    }
}
