/* codigo de usuario */
package analizadorLexico;
import analizadorLexico.TipoToken;

%% //separador de area

/* opciones y declaraciones de jflex */

%public
%class Lexer
%type TipoToken //el valor retornado por cada lectura será del tipo Token
%line
%column

LineTerminator = \r|\n|\r\n|null
WhiteSpace = {LineTerminator} | [ \t\f]
InputCharacter = [^\r\n]
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
Letra = [a-zA-Z]
Digito = [1-9][0-9]*
Negativo = [-]{Digito}
Identificador = ([_]|{Letra})({Letra}|{Digito}|[-]|[_])*
Numero = {Digito} | {Negativo} | "0"
Literal = "\"" {InputCharacter}* "\"" 

%{
    private String lexema;
    
    public String getLexema() {
        return lexema;
    }

    public int getLine(){
    	return yyline + 1;
    }

    public int getColum(){
    	return yycolumn + 1;
    }

%}

%% // separador de areas

/* reglas lexicas */
<YYINITIAL> {

	"ESCRIBIR" {return TipoToken.ESCRIBIR;}
	"FIN" {return TipoToken.FIN;}
	"REPETIR" {return TipoToken.REPETIR;}
	"INICIAR" {return TipoToken.INICIAR;}
	"SI" {return TipoToken.SI;}
	"VERDADERO"	{return TipoToken.VERDADERO;}
	"FALSO" {return TipoToken.FALSO;}
	"ENTONCES" {return TipoToken.ENTONCES;}
	"=" {return TipoToken.IGUAL;}
	"+" {return TipoToken.SUMA;}
	"*" {return TipoToken.MULTIPLICACION;}
	"-" {return TipoToken.RESTA;}
	"/" {return TipoToken.DIVISION;}
	"(" {return TipoToken.PARENTESIS_IZQUIERDO;}
	")" {return TipoToken.PARENTESIS_DERECHO;}
	"[" {return TipoToken.CORCHETE_IZQUIERDO;}
	"]" {return TipoToken.CORCHETE_DERECHO;}
	"{" {return TipoToken.LLAVE_IZQUIERDA;}
	"}" {return TipoToken.LLAVE_DERECHA;}
	"." {return TipoToken.PUNTO;}
	"," {return TipoToken.COMA;}
	";" {return TipoToken.PUNTO_Y_COMA;}
	":" {return TipoToken.DOS_PUNTOS;}
	{Numero} {lexema = yytext(); return TipoToken.NUMERO;}
	{Literal} {lexema = yytext(); return TipoToken.LITERAL;}
	{Identificador} {lexema = yytext(); return TipoToken.IDENTIFICADOR;}
	{EndOfLineComment} {lexema = yytext(); return TipoToken.COMENTARIO;}
	{WhiteSpace} 		{/*Nothing to do*/}
	{LineTerminator} 	{/*Nothing to do*/}

}
[^] {lexema = yytext(); return TipoToken.ERROR;}
