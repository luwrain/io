
grammar Latex;

@header{
package org.luwrain.antlr.latex;
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
