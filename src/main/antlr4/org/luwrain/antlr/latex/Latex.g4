
grammar Latex;

/*
@header{
package org.luwrain.antlr.latex;
}
*/

math
    : mathUnit* EOF
    ;

mathUnit
: Num | MathOp | command | block
;

command
: '\\' Ident
;

block
: '{' mathUnit* '}'
;

Num
: [0-9]
;

MathOp
: [+*]
;

Ident
: [a-zA-Z]+
;

WS
    : [ \t\r\n]+ -> skip
    ;
