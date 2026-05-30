import java_cup.runtime.Symbol;

%%

%class scanner
%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline + 1, yycolumn + 1, yytext());
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }
%}

%state COMMENT

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator}|[ \t\f]
Identifier = [A-Za-z_][A-Za-z0-9_]*
IntegerLiteral = 0|[1-9][0-9]*
FloatLiteral = ([0-9]+\.[0-9]+)
StringLiteral = \"([^\"\\]|\\.)*\"

%%

<YYINITIAL> {
    {WhiteSpace}+         { }
    "//"[^\r\n]*        { }
    "/*"                { yybegin(COMMENT); }

    "int"               { return symbol(sym.INT); }
    "float"             { return symbol(sym.FLOAT); }
    "boolean"           { return symbol(sym.BOOLEAN); }
    "void"              { return symbol(sym.VOID); }
    "if"                { return symbol(sym.IF); }
    "else"              { return symbol(sym.ELSE); }
    "while"             { return symbol(sym.WHILE); }
    "return"            { return symbol(sym.RETURN); }
    "true"              { return symbol(sym.TRUE); }
    "false"             { return symbol(sym.FALSE); }

    "&&"                { return symbol(sym.AND); }
    "||"                { return symbol(sym.OR); }
    "=="                { return symbol(sym.EQ); }
    "!="                { return symbol(sym.NEQ); }
    "<="                { return symbol(sym.LTE); }
    ">="                { return symbol(sym.GTE); }
    "="                 { return symbol(sym.ASSIGN); }
    "<"                 { return symbol(sym.LT); }
    ">"                 { return symbol(sym.GT); }
    "!"                 { return symbol(sym.NOT); }
    "+"                 { return symbol(sym.PLUS); }
    "-"                 { return symbol(sym.MINUS); }
    "*"                 { return symbol(sym.TIMES); }
    "/"                 { return symbol(sym.DIVIDE); }

    ";"                 { return symbol(sym.SEMI); }
    ","                 { return symbol(sym.COMMA); }
    "("                 { return symbol(sym.LPAREN); }
    ")"                 { return symbol(sym.RPAREN); }
    "{"                 { return symbol(sym.LBRACE); }
    "}"                 { return symbol(sym.RBRACE); }

    {FloatLiteral}       { return symbol(sym.FLOAT_LITERAL, Double.parseDouble(yytext())); }
    {IntegerLiteral}     { return symbol(sym.INT_LITERAL, Integer.parseInt(yytext())); }
    {StringLiteral}      { return symbol(sym.STRING_LITERAL, yytext()); }
    {Identifier}         { return symbol(sym.ID, yytext()); }

    .                    { throw new RuntimeException("Caractere ilegal '" + yytext() + "' na linha " + (yyline + 1) + ", coluna " + (yycolumn + 1)); }
}

<COMMENT> {
    "*/"                { yybegin(YYINITIAL); }
    [^*]+                { }
    "*"                 { }
}
