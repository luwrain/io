
grammar TokenFilterNotation;

@header{
  package org.luwrain.antlr.rutoken;
}

notation
    : unit* EOF
    ;

unit
    : (alter | unitBase) '*'*
    ;

unitBase
    : Latin | Cyril | Space | Punc | Num
    ;

alter
    : '(' unitBase ('|' unitBase )* ')'
    ;

Cyril
    : [а-яА-Я]+
    ;

Latin
    : [a-zA-Z]+
    ;

Num
    : [0-9]+
    ;

Punc
    : [.,?!:;$%@()_+=\-°′″]
    ;

Space
    :   [ \t\r\n]+
    ;
