
grammar Lilypond;

@header{
package org.luwrain.antlr.ly;
}

score
    : command* EOF
    ;

command
    : '\\' IDENT value*
    ;

value
    : seq | primitive 
    ;

seq : '{' IDENT '}'
    ;

primitive
    : string
    ;

string
    : '"' ~('"')+ '"'
    ;


IDENT
    :   [A-Za-z0-9]+
    ;

WS
    :   [ \t\r\n]+ -> skip
    ;
