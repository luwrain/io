
grammar Lilypond;

@header{
package org.luwrain.antlr.ly;
}

music
    : musicUnit* EOF
    ;

musicUnit
: command | String
;

command
    : '\\' Ident 
    ;

seq
: '{' IDENT '}'
    ;

Ident
    :   [A-Za-z0-9]+
    ;

String
: '"' [ a-zA-Z0-9.]* '"'
;

WS
    :   [ \t\r\n]+ -> skip
    ;
