package analizadorLexico.backend;

import analizadorLexico.ErrorLexema;
import analizadorLexico.Lexer;
import analizadorLexico.TipoToken;
import analizadorLexico.Token;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author fabricio
 */
public class ManejadorLexer {

    private List<ErrorLexema> listaErrores;
    private List<Token> listaTokens;
    private Queue<Token> colaTokens;

    public ManejadorLexer() {
        this.listaErrores = new LinkedList<>();
        this.listaTokens = new LinkedList<>();
        this.colaTokens = new LinkedList();
    }

    public void iniciarAFD(String entrada) {
        this.listaErrores.clear();
        this.listaTokens.clear();
        boolean seguir = true;
        Token token = null;
        Lexer lexer = new Lexer(new StringReader(entrada));
        while (seguir) {
            try {
                TipoToken miToken = lexer.yylex();
                if (miToken == null) {
                    seguir = false;
                    break;
                }
                switch (miToken) {
                    case ERROR:
                        ErrorLexema el = new ErrorLexema(lexer.getLexema(), lexer.getColum(), lexer.getLine());
                        listaErrores.add(el);
                        break;
                    case IDENTIFICADOR:
                        agreagarToken(miToken, lexer);
                        break;
                    case LITERAL:
                        agreagarToken(miToken, lexer);
                        break;
                    case NUMERO:
                        agreagarToken(miToken, lexer);
                        break;
                    case COMENTARIO:
                        agreagarToken(miToken, lexer);
                        break;
                    case ESCRIBIR:
                        agreagarPalabraReservada(miToken, lexer);
                        break;
                    case FIN:
                        agreagarPalabraReservada(miToken, lexer);
                        break;
                    case REPETIR:
                        agreagarPalabraReservada(miToken, lexer);
                        break;
                    case INICIAR:
                        agreagarPalabraReservada(miToken, lexer);
                        break;
                    case SI:
                        agreagarPalabraReservada(miToken, lexer);
                        break;
                    case VERDADERO:
                        agreagarPalabraReservada(miToken, lexer);
                        break;
                    case FALSO:
                        agreagarPalabraReservada(miToken, lexer);
                        break;
                    case ENTONCES:
                        agreagarPalabraReservada(miToken, lexer);
                        break;
                    default:
                        token = new Token(miToken, miToken.getLexema(), lexer.getColum(), lexer.getLine());
                        listaTokens.add(token);
                        break;
                }
            } catch (IOException e) {
                seguir = false;
                break;
            }
        }
    }

    public void agreagarToken(TipoToken miToken, Lexer lexer) {
        Token token = new Token(miToken, lexer.getLexema(), lexer.getColum(), lexer.getLine());
        listaTokens.add(token);
    }

    public void agreagarPalabraReservada(TipoToken miToken, Lexer lexer) {
        Token token = new Token(miToken, miToken.getNombreToken(), lexer.getColum(), lexer.getLine());
        listaTokens.add(token);
    }

    public List<ErrorLexema> getListaErrores() {
        return listaErrores;
    }

    public void setListaErrores(List<ErrorLexema> listaErrores) {
        this.listaErrores = listaErrores;
    }

    public List<Token> getListaTokens() {
        return listaTokens;
    }

    public void setListaTokens(List<Token> listaTokens) {
        this.listaTokens = listaTokens;
    }

}
