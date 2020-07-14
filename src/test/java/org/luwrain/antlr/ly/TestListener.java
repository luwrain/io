
package org.luwrain.antlr.ly;// Generated from src/main/java/org/luwrain/antlr/ly/Lilypond.g4 by ANTLR 4.8

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

class TestListener implements LilypondListener
{
    @Override public void enterCommand(LilypondParser.CommandContext ctx) {}
    @Override public void exitCommand(LilypondParser.CommandContext ctx) {}

        @Override public void enterMusicUnit(LilypondParser.MusicUnitContext ctx) {}
            @Override public void exitMusicUnit(LilypondParser.MusicUnitContext ctx) {}

            @Override public void enterMusic(LilypondParser.MusicContext ctx) {}
            @Override public void exitMusic(LilypondParser.MusicContext ctx) {}

    


    



                @Override public void enterSeq(LilypondParser.SeqContext ctx) {}
    @Override public void exitSeq(LilypondParser.SeqContext ctx) {}



    
    @Override public void enterEveryRule(ParserRuleContext ctx) {}
    @Override public void exitEveryRule(ParserRuleContext ctx) {}
        @Override public void visitTerminal(TerminalNode node) {}

        @Override public void visitErrorNode(ErrorNode node)
    {
	throw new IllegalStateException(node.toString());
    }
    
}
