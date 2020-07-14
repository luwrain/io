
grammar Latex;

@header{
package org.luwrain.antlr.latex;
}

math
    : mathUnit* EOF
    ;

mathUnit
: Num | MathOp
;

Num
: [0-9]
;

MathOp
: [+*]
;




WS
    : [ \t\r\n]+ -> skip
    ;
