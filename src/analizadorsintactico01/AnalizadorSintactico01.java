package analizadorsintactico01;

import analizadorLexico.TipoToken;
import analizadorLexico.Token;
import analizadorLexico.frontend.Analizador;
import analizadorSintactico.backend.AutomataPila;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fabricio
 */
public class AnalizadorSintactico01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Analizador analizador = new Analizador();
//        analizador.setVisible(true);
//        
        Token token1 = new Token(TipoToken.ESCRIBIR, TipoToken.ESCRIBIR.getNombreToken(), 0, 0);
        Token token2 = new Token(TipoToken.LITERAL, "algo", 0, 0);
        Token token3 = new Token(TipoToken.NUMERO, "10", 0, 0);
        Token token4 = new Token(TipoToken.IDENTIFICADOR, "id", 0, 0);
        Token token5 = new Token(TipoToken.FIN, TipoToken.FIN.getNombreToken(), 0, 0);
        Token token6 = new Token(TipoToken.SI, TipoToken.SI.getNombreToken(), 0, 0);
        Token token7 = new Token(TipoToken.VERDADERO, "VERDADERO", 0, 0);
        Token token8 = new Token(TipoToken.ENTONCES, "ENTONCES", 0, 0);
        Token token9 = new Token(TipoToken.FIN, TipoToken.FIN.getNombreToken(), 0, 0);
        List<Token> lista = new LinkedList<>();
        lista.add(token6);
        lista.add(token7);
        lista.add(token8);
        lista.add(token1);
        lista.add(token2);
        lista.add(token5);
        lista.add(token9);
        lista.add(token1);
        lista.add(token4);
        lista.add(token5);
        lista.add(token1);
        lista.add(token2);
        lista.add(token5);
        AutomataPila ap = new AutomataPila(lista);
        ap.iniciarAPD();
    }
}
