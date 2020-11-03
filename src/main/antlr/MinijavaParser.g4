parser grammar MinijavaParser;
options {
    tokenVocab = MinijavaLexer;
}

goal: mainClass (classDeclaration|lambdaDeclaration)* EOF;
mainClass: CLASS identifier LBRACE PUBLIC STATIC VOID MAIN LPAREN STRING LBRACK RBRACK identifier RPAREN LBRACE statement RBRACE RBRACE;
classDeclaration: CLASS identifier (EXTENDS identifier)? LBRACE varDeclaration* methodDeclaration* statement* RBRACE;
lambdaDeclaration: LAMBDA identifier LBRACE (type|VOID) identifier param SEMIC RBRACE;
varDeclaration: type identifier SEMIC;
methodDeclaration: PUBLIC type identifier param LBRACE varDeclaration* statement* RETURN expression SEMIC RBRACE;
param: LPAREN (type identifier (COMMA type identifier)*)? RPAREN;
type: INTARR | BOOL | INT | identifier;
statement: LBRACE statement* RBRACE
         | IF LPAREN expression RPAREN statement ELSE statement
         | WHILE LPAREN expression RPAREN statement
         | PRINTLN LPAREN expression RPAREN SEMIC
         | identifier ASSIGN expression SEMIC
         | identifier LBRACK expression RBRACK ASSIGN expression SEMIC; //array assign
expression: expression OP expression
          | expression PM expression
          | expression LBRACK expression RBRACK
          | expression DOT LENGTH
          | lambda
          | expression DOT identifier LPAREN (expression (COMMA expression)*)? RPAREN
          | /*PM?*/ INTLIT
          | TRUE
          | FALSE
          | identifier
          | THIS
          | NEW INT LBRACK expression RBRACK
          | NEW identifier LPAREN RPAREN
          | NEG expression
          | LPAREN expression RPAREN;
lambda: param ARROW expression
      | param ARROW LBRACE varDeclaration* statement* RETURN expression SEMIC RBRACE;
identifier: IDENT;
