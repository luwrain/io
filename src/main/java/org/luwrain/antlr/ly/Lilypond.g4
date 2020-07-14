
grammar Lilypond;

@header{
package org.luwrain.antlr.ly;
}

music
    : musicUnit* EOF
    ;

musicUnit
: command | note | String
;

command
    : '\\' Ident 
    ;

seq
: '{' IDENT '}'
    ;

note
: NoteName Oct? Duration?
;

NoteName
: ('a' | 'b')
;

Oct
: (',' | ',,')
;

Duration
: ('1' | '2' | '4' | '8' | '16' | '32' | '64')
;

Ident
    :   [A-Za-z]+
    ;


String
: '"' [ a-zA-Z0-9.]* '"'
;

WS
    :   [ \t\r\n]+ -> skip
    ;
