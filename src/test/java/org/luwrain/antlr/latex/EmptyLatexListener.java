
package org.luwrain.antlr.latex;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

class EmptyLatexListener implements LatexListener
{
    @Override public void enterScore(LatexParser.ScoreContext ctx) {}
    @Override public void exitScore(LatexParser.ScoreContext ctx) {}
    @Override public void enterCommand(LatexParser.CommandContext ctx) {}
    @Override public void exitCommand(LatexParser.CommandContext ctx) {}
    @Override public void enterValue(LatexParser.ValueContext ctx) {}
    @Override public void exitValue(LatexParser.ValueContext ctx) {}

        @Override public void enterString(LatexParser.StringContext ctx) {}
    @Override public void exitString(LatexParser.StringContext ctx) {}

            @Override public void enterPrimitive(LatexParser.PrimitiveContext ctx) {}
    @Override public void exitPrimitive(LatexParser.PrimitiveContext ctx) {}

                @Override public void enterSeq(LatexParser.SeqContext ctx) {}
    @Override public void exitSeq(LatexParser.SeqContext ctx) {}



    
    @Override public void enterEveryRule(ParserRuleContext ctx) {}
    @Override public void exitEveryRule(ParserRuleContext ctx) {}
        @Override public void visitTerminal(TerminalNode node) {}

        @Override public void visitErrorNode(ErrorNode node)
    {
	throw new IllegalStateException(node.toString());
    }
    
}
