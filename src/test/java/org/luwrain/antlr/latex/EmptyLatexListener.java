
package org.luwrain.antlr.latex;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

class EmptyLatexListener implements LatexListener
{




                    @Override public void enterMathUnit(LatexParser.MathUnitContext ctx) {}
                        @Override public void exitMathUnit(LatexParser.MathUnitContext ctx) {}

                        @Override public void enterMath(LatexParser.MathContext ctx) {}
                        @Override public void exitMath(LatexParser.MathContext ctx) {}


    @Override public void enterEveryRule(ParserRuleContext ctx) {}
    @Override public void exitEveryRule(ParserRuleContext ctx) {}
        @Override public void visitTerminal(TerminalNode node) {}

    @Override public void visitErrorNode(ErrorNode node)    {
	throw new IllegalStateException(node.toString());
    }
}
