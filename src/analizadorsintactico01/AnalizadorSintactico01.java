package analizadorsintactico01;

import analizadorLexico.Lexer;
import analizadorLexico.Token;
import java.io.IOException;
import java.io.StringReader;

/**
 *
 * @author fabricio
 */
public class AnalizadorSintactico01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String input = "aaaababb abb\n"
                + "aaab\n"
                + "baababab\n"
                + "abababb\n"
                + "babbbbbbabb\n"
                + "asdasd\n"
                + "abaaabb 02300\n"
                + "\n"
                + "sumando = 25\n"
                + "id = sumando + 32.25\n"
                + "//un comentario\n"
                + "ESCRIBIR\n"
                + "FIN\n"
                + "ENTONCES\n"
                + "\"Una literal\"\n"
                + "25as.dasds3 = sumando2.";

        boolean seguir = true;
        Lexer analizador = new Lexer(new StringReader(input));
        while (seguir) {
            try {
                Token miToken = analizador.yylex();
                if (miToken == null) {
                    seguir = false;
                    break;
                }
                switch (miToken) {
                    case ERROR:
                        System.out.printf("Se encontro un error, lexema: [%s]\n", analizador.yytext());
                        break;
                    case IDENTIFICADOR:
                    case LITERAL:
                    case NUMERO:
                    case COMENTARIO:
                    case ESCRIBIR:
                    case FIN:
                    case REPETIR:
                    case INICIAR:
                    case SI:
                    case VERDADERO:
                    case FALSO:
                    case ENTONCES:
                        System.out.printf("token del tipo [%s], lexema es [%s]\n", miToken.getNombreToken(), analizador.getLexema());
                        break;
                    default:
                        System.out.printf("token del tipo [%s], solo hay un lexema el cual es [%s]\n", miToken.getNombreToken(), analizador.yytext());
                        break;
                }
            } catch (IOException e) {
                seguir = false;
                break;
            }

        }
    }

}
