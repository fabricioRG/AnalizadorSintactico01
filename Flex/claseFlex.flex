/* codigo de usuario */
package analizadorLexico;
import analizadorLexico.Token;

%% //separador de area

/* opciones y declaraciones de jflex */

%public
%class Lexer
%type Token //el valor retornado por cada lectura será del tipo Token
%line
%column


LineTerminator = \r|\n|\r\n|null
WhiteSpace = {LineTerminator} | [ \t\f]
InputCharacter = [^\r\n]
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
Letra = [a-zA-Z]
Digito = [1-9][0-9]*
Negativo = [-]{Digito}
Identificador = {Letra}({Letra}|{Digito})*
Numero = {Digito} | {Negativo} | "0"
Literal = "\"" {InputCharacter}* "\"" 

%{
    private String lexema;
    
    public String getLexema() {
        return lexema;
    }
%}

%% // separador de areas

/* reglas lexicas */
<YYINITIAL> {

	"ESCRIBIR" {return Token.ESCRIBIR;}
	"FIN" {return Token.FIN;}
	"REPETIR" {return Token.REPETIR;}
	"INICIAR" {return Token.INICIAR;}
	"SI" {return Token.SI;}
	"VERDADERO"	{return Token.VERDADERO;}
	"FALSO" {return Token.FALSO;}
	"ENTONCES" {return Token.ENTONCES;}
	"=" {return Token.IGUAL;}
	"+" {return Token.SUMA;}
	"*" {return Token.MULTIPLICACION;}
	"-" {return Token.RESTA;}
	"/" {return Token.DIVISION;}
	{Numero} {lexema = yytext(); return Token.NUMERO;}
	{Literal} {lexema = yytext(); return Token.LITERAL;}
	{Identificador} {lexema = yytext(); return Token.IDENTIFICADOR;}
	{EndOfLineComment} {lexema = yytext(); return Token.COMENTARIO;}
	{WhiteSpace} 		{/*Nothing to do*/}
	{LineTerminator} 	{/*Nothing to do*/}

}
[^] {lexema = yytext(); return Token.ERROR;}
